package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.gateway_playlist.*
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import kotlin.random.Random

@GrpcService
class GRPCGatewayRequestService : GatewayPlaylistServiceGrpc.GatewayPlaylistServiceImplBase() {
    override fun getPlaylist(
        request: RequestGetPlaylist,
        responseObserver: StreamObserver<ResponseFullPlaylist>,
    ) {
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
                })
                .build()
        )
        responseObserver.onCompleted()
    }
}