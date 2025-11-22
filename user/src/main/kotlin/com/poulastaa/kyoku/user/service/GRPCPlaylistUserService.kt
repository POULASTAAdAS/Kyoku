package com.poulastaa.kyoku.user.service

import com.poulastaa.kyoku.grpc.playlistUser.EmptyResponse
import com.poulastaa.kyoku.grpc.playlistUser.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlistUser.RequestSaveUserPlaylist
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory

@GrpcService
class GRPCPlaylistUserService(

) : PlaylistUserServiceGrpc.PlaylistUserServiceImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun saveUserPlaylist(
        request: RequestSaveUserPlaylist?,
        responseObserver: StreamObserver<EmptyResponse>,
    ) {

    }
}