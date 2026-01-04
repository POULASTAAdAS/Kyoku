package com.poulastaa.kyoku.playlist.service

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import com.poulastaa.kyoku.grpc.gateway_playlist.*
import com.poulastaa.kyoku.grpc.model.RequestUser
import com.poulastaa.kyoku.grpc.playlist_user.EmptyResponse
import com.poulastaa.kyoku.grpc.playlist_user.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlist_user.RequestSaveUserPlaylist
import com.poulastaa.kyoku.playlist.database.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.database.entity.PlaylistVisibility
import com.poulastaa.kyoku.playlist.database.repository.PlaylistDataSource
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import jakarta.transaction.Transactional
import net.devh.boot.grpc.server.service.GrpcService
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@GrpcService
class GRPCGatewayRequestService(
    private val db: PlaylistDataSource,
) : GatewayPlaylistServiceGrpc.GatewayPlaylistServiceImplBase() {
    private lateinit var userServiceStub: PlaylistUserServiceGrpc.PlaylistUserServiceFutureStub
    private val userService: PlaylistUserServiceGrpc.PlaylistUserServiceFutureStub
        get() = userServiceStub.withDeadlineAfter(5, TimeUnit.SECONDS)

    @Transactional
    override fun getPlaylist(
        request: RequestGetPlaylist,
        responseObserver: StreamObserver<ResponseFullPlaylist>,
    ) {
        // extract songs from playlistId

        //1. save playlist
        val existingPlaylists = db.count()
        val dbPlaylist = db.save(
            EntityPlaylist(
                name = "Playlist #${existingPlaylists + 1}",
                description = "Enjoy your imported playlist",
                visibility = PlaylistVisibility.PRIVATE.status,
                totalSongs = 0, // TODO
                totalDuration = 0, // TODO
            )
        )
        //2. save playlistId + songId

        //3. save userId + playlistId to user-service


        Futures.addCallback(
            userService.saveUserPlaylist(RequestSaveUserPlaylist.newBuilder().apply {
                this.playlistId = dbPlaylist.id
                this.user = RequestUser.newBuilder().apply {
                    this.email = request.user.email
                    this.type = request.user.type
                }.build()
            }.build()),
            object : FutureCallback<EmptyResponse> {
                override fun onSuccess(result: EmptyResponse?) {
                    // todo add actual playlist latter
                    responseObserver.onNext(
                        ResponseFullPlaylist.newBuilder()
                            .setPlaylist(
                                ResponsePlaylist.newBuilder()
                                    .setPlaylistId(1)
                                    .setName("Playlist #385")
                                    .setPopularity(4279782)
                                    .setStatus(ResponsePlaylist.ResponsePlaylistVisibilityState.PRIVATE)
                                    .build()
                            )
                            .addAllSongs((1..10).map {
                                ResponseSong.newBuilder().apply {
                                    this.songId = it.toLong()
                                    this.title = "song $it"
                                    this.masterPlaylist = "/master-playlist/song_$it.m3u8"
                                    if (Random.nextBoolean()) this.poster = "/image/song/$it.jpg"
                                }.build()
                            }).build()
                    )
                    responseObserver.onCompleted()
                }

                override fun onFailure(t: Throwable) {
                    val status = when (t) {
                        is StatusRuntimeException -> t.status
                        else -> Status.INTERNAL.withDescription("Failed to save user playlist: ${t.message}")
                    }

                    responseObserver.onError(status.withCause(t).asRuntimeException())
                }
            },
            MoreExecutors.directExecutor()
        )

    }
}