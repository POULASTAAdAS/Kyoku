package com.poulastaa.kyoku.activity.service

import com.poulastaa.kyoku.activity.database.activity.entity.EntityUserGenre
import com.poulastaa.kyoku.activity.database.activity.repository.UserGenreDatasource
import com.poulastaa.kyoku.grpc.activity_save_genre.SaveGenreGrpc
import com.poulastaa.kyoku.grpc.model.EmptyResponse
import com.poulastaa.kyoku.grpc.model.RequestSaveUserGenre
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GRPCSaveGenre(
    private val db: UserGenreDatasource,
) : SaveGenreGrpc.SaveGenreImplBase() {
    override fun storeUserGenre(
        request: RequestSaveUserGenre,
        responseObserver: StreamObserver<EmptyResponse>,
    ) {
        try {
            db.save(
                EntityUserGenre(
                    userId = request.userId,
                    genreIds = request.genreListList
                )
            )

            responseObserver.onNext(EmptyResponse.newBuilder().build())
            responseObserver.onCompleted()
        } catch (e: Exception) {
            responseObserver.onError(
                Status.ABORTED.withDescription(e.message ?: "Something went wrong").asRuntimeException()
            )
        }
    }
}