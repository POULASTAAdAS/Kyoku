package com.poulastaa.kyoku.user.service

import com.poulastaa.kyoku.grpc.playlistUser.EmptyResponse
import com.poulastaa.kyoku.grpc.playlistUser.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlistUser.RequestSaveUserPlaylist
import com.poulastaa.kyoku.grpc.playlistUser.RequestUser
import com.poulastaa.kyoku.user.database.playlist.entity.EntityUserPlaylist
import com.poulastaa.kyoku.user.database.playlist.entity.ids.UserPlaylistId
import com.poulastaa.kyoku.user.database.playlist.repository.PlaylistDataSource
import com.poulastaa.kyoku.user.database.playlist.repository.UserPlaylistDataSource
import com.poulastaa.kyoku.user.database.user.repository.UserDataSource
import com.poulastaa.kyoku.user.database.user.repository.UserTypeDataSource
import com.poulastaa.kyoku.user.utils.DebugUtils
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@GrpcService
class GRPCPlaylistUserService(
    private val userPlaylist: UserPlaylistDataSource,
    private val playlist: PlaylistDataSource,
    private val user: UserDataSource,
    private val userType: UserTypeDataSource,
) : PlaylistUserServiceGrpc.PlaylistUserServiceImplBase() {
    @Transactional
    override fun saveUserPlaylist(
        request: RequestSaveUserPlaylist,
        responseObserver: StreamObserver<EmptyResponse>,
    ) {
        val playlist = playlist.findById(request.playlistId).getOrNull() ?: let {
            responseObserver.onError(
                Status.NOT_FOUND.withDescription("Playlist not found ${request.playlistId}").asRuntimeException()
            )
            return
        }

        val userType = when (request.user.type) {
            RequestUser.UserType.EMAIL -> request.user.type.name
            RequestUser.UserType.GOOGLE -> request.user.type.name
            else -> {
                responseObserver.onError(
                    Status.NOT_FOUND.withDescription("UserType not Allowed").asRuntimeException()
                )
                return
            }
        }.let {
            userType.findByTypeIgnoreCase(it)
        } ?: run {
            responseObserver.onError(Status.NOT_FOUND.withDescription("UserType not found").asRuntimeException())
            return
        }

        val user = user.findByUserTypeAndEmail(
            userType = userType,
            email = request.user.email
        )

        val result = userPlaylist.save(
            EntityUserPlaylist(
                id = UserPlaylistId(
                    userId = user.id,
                    playlistId = request.playlistId,
                ),
                playlist = playlist
            )
        )

        DebugUtils.d("user playlist saved: $result")

        responseObserver.onNext(EmptyResponse.newBuilder().build())
        responseObserver.onCompleted()
    }
}