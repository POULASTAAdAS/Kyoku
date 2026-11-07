package com.poulastaa.kyoku.search.database.repository

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery
import com.poulastaa.kyoku.search.domain.model.dto.DtoSongMatch
import com.poulastaa.kyoku.search.domain.model.dto.DtoTrackQuery
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

private const val MSEARCH_CHUNK_SIZE = 100

private const val FIELD_TITLE = "title"
private const val FIELD_ARTIST_NAME = "artists.name"
private const val FIELD_ALBUM_NAME = "album.name"
private const val FIELD_POPULARITY = "popularity"

// same exclusion list as the old playlist REGEXP matcher; skipped when the source title itself contains the word
private val EXCLUDED_VARIANTS = listOf("remix", "mashup", "lofi", "slowed", "new years", "reverb", "8d", "nightcore")

@Service
class SongESRepository(
    private val client: ElasticsearchClient,
    @param:Value("\${elasticsearch.song.index}")
    private val index: String,
    @param:Value("\${elasticsearch.song.min-score}")
    private val minScore: Double,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Best matching song per track (one `_msearch` per chunk, top hit only, `min_score` filtered).
     * Tracks without a hit above [minScore] are omitted.
     */
    fun matchSongs(tracks: List<DtoTrackQuery>): List<DtoSongMatch> = tracks
        .filter { it.title.isNotBlank() }
        .chunked(MSEARCH_CHUNK_SIZE)
        .flatMap { chunk -> matchChunk(chunk) }

    private fun matchChunk(tracks: List<DtoTrackQuery>): List<DtoSongMatch> {
        val response = client.msearch(
            { req ->
                req.index(index)
                tracks.forEach { track ->
                    req.searches { item ->
                        item.header { it }
                            .body { body ->
                                body.size(1)
                                    .minScore(minScore)
                                    .trackScores(true)
                                    .source { it.fetch(false) }
                                    .query { q -> q.bool { bool -> bool.trackQuery(track) } }
                                    .sort { s -> s.score { it.order(SortOrder.Desc) } }
                                    .sort { s -> s.field { it.field(FIELD_POPULARITY).order(SortOrder.Desc) } }
                            }
                    }
                }
                req
            },
            Void::class.java
        )

        return response.responses().mapIndexedNotNull { i, item ->
            if (item.isFailure) {
                logger.warn("song match failed for position ${tracks[i].position}: ${item.failure().error().reason()}")
                return@mapIndexedNotNull null
            }

            item.result().hits().hits().firstOrNull()?.let { hit ->
                DtoSongMatch(
                    position = tracks[i].position,
                    songId = hit.id()!!.toLong(),
                    score = hit.score() ?: 0.0
                )
            }
        }
    }

    private fun BoolQuery.Builder.trackQuery(track: DtoTrackQuery): BoolQuery.Builder {
        val rawTitle = track.title.lowercase()
        val title = rawTitle.normalizeTitle().ifBlank { rawTitle }

        must { m ->
            m.match {
                it.field(FIELD_TITLE)
                    .query(title)
                    .fuzziness("AUTO")
                    .minimumShouldMatch("75%")
            }
        }

        track.artists.filter { it.isNotBlank() }.forEach { artist ->
            should { s -> s.match { it.field(FIELD_ARTIST_NAME).query(artist) } }
        }

        track.album?.takeIf { it.isNotBlank() }?.let { album ->
            should { s -> s.match { it.field(FIELD_ALBUM_NAME).query(album) } }
        }

        EXCLUDED_VARIANTS.filterNot { rawTitle.contains(it) }.forEach { variant ->
            mustNot { mn -> mn.matchPhrase { it.field(FIELD_TITLE).query(variant) } }
        }

        return this
    }

    /**
     * Spotify titles carry decorations the content catalogue does not:
     * `Song - Remastered 2011`, `Song (feat. X)`, `Song (From "Movie")`, punctuation.
     */
    private fun String.normalizeTitle() = this
        .replace(Regex("\\s+-\\s+.*$"), "")
        .replace(Regex("[(\\[].*?[)\\]]"), " ")
        .replace(Regex("[^\\p{L}\\p{N}\\s]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}
