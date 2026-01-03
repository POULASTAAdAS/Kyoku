package com.poulastaa.kyoku.user.service

import com.poulastaa.kyoku.grpc.playlistUser.EmptyResponse
import com.poulastaa.kyoku.grpc.playlistUser.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlistUser.RequestSaveUserPlaylist
import com.poulastaa.kyoku.user.database.entity.EntityUserPlaylist
import com.poulastaa.kyoku.user.database.entity.ids.EntityUserPlaylistId
import com.poulastaa.kyoku.user.database.repository.UserPlaylistDataSource
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@GrpcService
class GRPCPlaylistUserService(
    private val db: UserPlaylistDataSource,
) : PlaylistUserServiceGrpc.PlaylistUserServiceImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun saveUserPlaylist(
        request: RequestSaveUserPlaylist,
        responseObserver: StreamObserver<EmptyResponse>,
    ) {
        db.save(
            EntityUserPlaylist(
                id = EntityUserPlaylistId(
                    userId = request.userId,
                    playlistId = request.playlistId,
                )
            )
        )

        responseObserver.onNext(EmptyResponse.newBuilder().build())
        responseObserver.onCompleted()
    }
}