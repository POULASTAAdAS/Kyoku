package com.poulastaa.kyoku.search.service

import co.elastic.clients.elasticsearch._types.ElasticsearchException
import com.poulastaa.kyoku.grpc.playlist_search.Match
import com.poulastaa.kyoku.grpc.playlist_search.PlaylistSearchServiceGrpc
import com.poulastaa.kyoku.grpc.playlist_search.RequestMatchSongs
import com.poulastaa.kyoku.grpc.playlist_search.ResponseMatchSongs
import com.poulastaa.kyoku.search.database.repository.SongESRepository
import com.poulastaa.kyoku.search.domain.model.dto.DtoTrackQuery
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import java.io.IOException

@GrpcService
class GRPCPlaylistRequestService(
    private val es: SongESRepository,
) : PlaylistSearchServiceGrpc.PlaylistSearchServiceImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun matchSongs(
        request: RequestMatchSongs,
        responseObserver: StreamObserver<ResponseMatchSongs>,
    ) {
        if (request.tracksCount == 0) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT.withDescription("No tracks to match").asRuntimeException()
            )
            return
        }

        try {
            val matches = es.matchSongs(
                request.tracksList.map { track ->
                    DtoTrackQuery(
                        position = track.position,
                        title = track.title,
                        artists = track.artistsList,
                        album = if (track.hasAlbum()) track.album else null
                    )
                }
            )

            responseObserver.onNext(
                ResponseMatchSongs.newBuilder().apply {
                    addAllMatches(
                        matches.map { match ->
                            Match.newBuilder().apply {
                                this.position = match.position
                                this.songId = match.songId
                                this.score = match.score.toFloat()
                            }.build()
                        }
                    )
                }.build()
            )
            responseObserver.onCompleted()
        } catch (e: IOException) {
            logger.error("Elasticsearch unavailable", e)
            responseObserver.onError(
                Status.UNAVAILABLE.withDescription("Search index unavailable").withCause(e).asRuntimeException()
            )
        } catch (e: ElasticsearchException) {
            logger.error("Elasticsearch song match failed", e)
            responseObserver.onError(
                Status.INTERNAL.withDescription("Song match failed").withCause(e).asRuntimeException()
            )
        } catch (e: Exception) {
            logger.error("Song match failed", e)
            responseObserver.onError(
                Status.INTERNAL.withDescription("Song match failed").withCause(e).asRuntimeException()
            )
        }
    }
}
