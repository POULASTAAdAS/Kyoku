package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.playlist_search.PlaylistSearchServiceGrpc
import com.poulastaa.kyoku.grpc.playlist_search.RequestMatchSongs
import com.poulastaa.kyoku.grpc.playlist_search.Track
import com.poulastaa.kyoku.playlist.domain.model.DtoSpotifyTrack
import com.poulastaa.kyoku.playlist.utils.DebugUtils
import com.poulastaa.kyoku.playlist.utils.SongId
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

private const val SEARCH_DEADLINE_SECONDS = 5L

/**
 * Maps source playlist tracks to catalogue song ids.
 * Primary: `search` service (Elasticsearch). Fallback when `search` is unreachable: CONTENT DB title match.
 */
@Component
class SongMatcher(
    private val playlistRepo: PlaylistRepository,
) {
    @GrpcClient("search")
    private lateinit var searchStub: PlaylistSearchServiceGrpc.PlaylistSearchServiceBlockingStub

    /**
     * @return matched song ids in source playlist order, without duplicates
     */
    fun match(tracks: List<DtoSpotifyTrack>): List<SongId> {
        if (tracks.isEmpty()) return emptyList()

        val ids = try {
            matchWithSearch(tracks)
        } catch (e: StatusRuntimeException) {
            if (e.status.code !in FALLBACK_CODES) throw e

            DebugUtils.d("search unavailable (${e.status.code}), falling back to db title match")
            playlistRepo.findSongIdsByTitles(tracks.map { it.title })
        }

        return ids.distinct()
    }

    private fun matchWithSearch(tracks: List<DtoSpotifyTrack>): List<SongId> {
        val response = searchStub.withDeadlineAfter(SEARCH_DEADLINE_SECONDS, TimeUnit.SECONDS)
            .matchSongs(
                RequestMatchSongs.newBuilder().apply {
                    addAllTracks(
                        tracks.map { track ->
                            Track.newBuilder().apply {
                                position = track.position
                                title = track.title
                                addAllArtists(track.artists)
                                track.album?.let { album = it }
                            }.build()
                        }
                    )
                }.build()
            )

        return response.matchesList
            .sortedBy { it.position }
            .map { it.songId }
    }

    private companion object {
        val FALLBACK_CODES = setOf(
            Status.Code.UNAVAILABLE,
            Status.Code.DEADLINE_EXCEEDED,
            Status.Code.UNIMPLEMENTED,
            Status.Code.INTERNAL,
        )
    }
}
