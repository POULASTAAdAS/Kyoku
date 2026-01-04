package com.poulastaa.kyoku.user.service

import com.poulastaa.kyoku.grpc.playlistUser.EmptyResponse
import com.poulastaa.kyoku.grpc.playlistUser.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlistUser.RequestSaveUserPlaylist
import com.poulastaa.kyoku.user.database.entity.EntityUserPlaylist
import com.poulastaa.kyoku.user.database.entity.ids.EntityUserPlaylistId
import com.poulastaa.kyoku.user.database.repository.UserPlaylistDataSource
import com.poulastaa.kyoku.user.utils.DebugUtils
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.transaction.annotation.Transactional

@GrpcService
class GRPCPlaylistUserService(
    private val db: UserPlaylistDataSource,
) : PlaylistUserServiceGrpc.PlaylistUserServiceImplBase() {
    @Transactional
    override fun saveUserPlaylist(
        request: RequestSaveUserPlaylist,
        responseObserver: StreamObserver<EmptyResponse>,
    ) {
        val result = db.save(
            EntityUserPlaylist(
                id = EntityUserPlaylistId(
                    userId = request.userId,
                    playlistId = request.playlistId,
                )
            )
        )

        DebugUtils.d("user playlist saved: $result")

        responseObserver.onNext(EmptyResponse.newBuilder().build())
        responseObserver.onCompleted()
    }
}