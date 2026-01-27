package com.poulastaa.kyoku.content.service.grpc

import com.poulastaa.kyoku.content.service.setup.SetupService
import com.poulastaa.kyoku.grpc.gateway_setup.GatewaySetupServiceGrpc
import com.poulastaa.kyoku.grpc.model.GenreRequest
import com.poulastaa.kyoku.grpc.model.GenreResponse
import com.poulastaa.kyoku.grpc.model.ResponseGenre
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcGenreService(
    private val service: SetupService,
) : GatewaySetupServiceGrpc.GatewaySetupServiceImplBase() {
    override fun getGenre(
        request: GenreRequest,
        responseObserver: StreamObserver<GenreResponse>,
    ) {
        try {
            val pageData = service.getGenre(
                page = request.page,
                size = request.size,
                query = request.query
            )

            responseObserver.onNext(
                GenreResponse.newBuilder().apply {
                    hasMore = pageData.second
                    addAllList(
                        pageData.first.map { dto ->
                            ResponseGenre.newBuilder().apply {
                                this.id = dto.id
                                this.type = dto.type
                                this.poster?.let { this.poster = it }
                                this.popularity = dto.popularity
                            }.build()
                        }
                    )
                }.build()
            )

            responseObserver.onCompleted()
        } catch (e: Exception) {
            e.printStackTrace()

            responseObserver.onError(
                Status.INTERNAL.withDescription(e.message ?: "something went wrong").asRuntimeException()
            )
        }
    }
}