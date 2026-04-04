package com.poulastaa.kyoku.search.service

import com.poulastaa.kyoku.grpc_search.GatewaySearchServiceGrpc
import com.poulastaa.kyoku.grpc_search.RequestImportArtist
import com.poulastaa.kyoku.grpc_search.ResponseArtist
import com.poulastaa.kyoku.grpc_search.ResponseImportArtist
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

private const val MAX_IMPORT_ARTIST_LIMIT = 29

@GrpcService
class GRPCGatewayRequestService(
    private val cache: RedisCacheService,
) : GatewaySearchServiceGrpc.GatewaySearchServiceImplBase() {
    override fun importArtist(
        request: RequestImportArtist,
        responseObserver: StreamObserver<ResponseImportArtist>,
    ) {
        // if page is 0 and with empty query check cache
        val cacheArtist = cache.getMostPopularArtistByCountry(
            size = request.limit,
            page = request.page,
            key = request.countryCode // TODO: get actual country from code
        )

        if (request.q.isBlank()) {
            if (cacheArtist.isNullOrEmpty().not()) responseObserver.onNext(
                ResponseImportArtist.newBuilder().apply {
                    addAllArtists(
                        cacheArtist.map { dto ->
                            ResponseArtist.newBuilder().apply {
                                this.id = dto.id
                                this.name = dto.name
                                this.cover = dto.cover
                                this.popularity = dto.popularity
                            }.build()
                        }
                    )
                }.build()
            ) else {
                // miss
                // query database with max limit (MAX_IMPORT_ARTIST_LIMIT) also put in cache

            }

            responseObserver.onCompleted()
            return
        }

        // has query string
        // query database directly

    }
}