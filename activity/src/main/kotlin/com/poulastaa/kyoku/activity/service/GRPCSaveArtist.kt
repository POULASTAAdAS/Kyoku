package com.poulastaa.kyoku.activity.service

import com.poulastaa.kyoku.activity.database.activity.entity.EntityUserArtist
import com.poulastaa.kyoku.activity.database.activity.repository.UserArtistDatasource
import com.poulastaa.kyoku.grpc.activity_save_artist.SaveArtistGrpc
import com.poulastaa.kyoku.grpc.model.EmptyResponse
import com.poulastaa.kyoku.grpc.model.RequestSaveUserArtist
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GRPCSaveArtist(
    private val db: UserArtistDatasource,
) : SaveArtistGrpc.SaveArtistImplBase() {
    override fun storeUserArtist(
        request: RequestSaveUserArtist,
        responseObserver: StreamObserver<EmptyResponse>,
    ) {
        try {
            db.save(
                EntityUserArtist(
                    userId = request.userId,
                    artistIds = request.artistIdListList
                )
            )

            println("user artists saved on userId: ${request.userId}")

            responseObserver.onNext(EmptyResponse.newBuilder().build())
            responseObserver.onCompleted()
        } catch (e: Exception) {
            e.printStackTrace()
            responseObserver.onError(
                Status.ABORTED.withDescription(e.message ?: "Something went wrong").asRuntimeException()
            )
        }
    }
}