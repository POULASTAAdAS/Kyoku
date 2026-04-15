package com.poulastaa.kyoku.search.service

import com.poulastaa.kyoku.grpc_search.GatewaySearchServiceGrpc
import com.poulastaa.kyoku.grpc_search.RequestImportArtist
import com.poulastaa.kyoku.grpc_search.ResponseArtist
import com.poulastaa.kyoku.grpc_search.ResponseImportArtist
import com.poulastaa.kyoku.search.database.repository.ContentESRepository
import com.poulastaa.kyoku.search.database.repository.ContentJPARepository
import com.poulastaa.kyoku.search.domain.model.dto.DtoCountry
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

private const val MAX_IMPORT_ARTIST_LIMIT = 30

@GrpcService
class GRPCGatewayRequestService(
    private val db: ContentJPARepository,
    private val es: ContentESRepository,
    private val cache: RedisCacheService,
) : GatewaySearchServiceGrpc.GatewaySearchServiceImplBase() {
    override fun importArtist(
        request: RequestImportArtist,
        responseObserver: StreamObserver<ResponseImportArtist>,
    ) {
        val country = getCountry(request.countryCode) ?: run {
            responseObserver.onError(Status.UNKNOWN.withDescription("Invalid country code").asRuntimeException())
            responseObserver.onCompleted()
            return
        }

        if (request.q.isBlank()) {
            // if empty query check cache
            val cacheArtist = cache.getMostPopularArtistByCountry(
                size = request.limit,
                page = request.page,
                key = country.name
            )

            val result = if (cacheArtist.isNullOrEmpty().not()) cacheArtist else {
                // miss
                // query database with max limit (MAX_IMPORT_ARTIST_LIMIT + 1) also put in cache
                val dbList = es.getMostPopularArtistByCountry(MAX_IMPORT_ARTIST_LIMIT + 1, country.code)
                cache.setMostPopularArtistsByCountry(dbList, country.name)

                dbList.drop(request.page * request.limit).take(request.limit)
            }

            responseObserver.onNext(
                ResponseImportArtist.newBuilder().apply {
                    addAllArtists(
                        result.map { dto ->
                            ResponseArtist.newBuilder().apply {
                                this.id = dto.id
                                this.name = dto.name
                                this.cover = dto.cover
                                this.popularity = dto.popularity
                            }.build()
                        }
                    )
                }.build()
            )

            responseObserver.onCompleted()
            return
        }

        // has query string
        // query database directly

    }

    private fun getCountry(countryCode: String): DtoCountry? {
        val cachedCountry = cache.getAllCountries()

        return if (cachedCountry == null) {
            val dbList = db.getAllCountry().map { it.toDtoCountry() }
            cache.setAllCountries(dbList)
            dbList.firstOrNull { it.code == countryCode }
        } else cachedCountry.firstOrNull { it.code == countryCode }
    }
}