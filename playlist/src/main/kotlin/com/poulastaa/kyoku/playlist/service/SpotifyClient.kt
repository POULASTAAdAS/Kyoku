package com.poulastaa.kyoku.playlist.service

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.poulastaa.kyoku.playlist.domain.model.DtoSpotifyPlaylist
import com.poulastaa.kyoku.playlist.domain.model.DtoSpotifyTrack
import com.poulastaa.kyoku.playlist.domain.model.ImportPlaylistException
import com.poulastaa.kyoku.playlist.domain.model.internal.SpotifyEmbedPage
import com.poulastaa.kyoku.playlist.utils.DebugUtils
import io.grpc.Status
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

private const val SPOTIFY_EMBED_PLAYLIST_URL = "https://open.spotify.com/embed/playlist"
private const val SPOTIFY_EMBED_USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0 Safari/537.36"
private const val SPOTIFY_ARTIST_SEPARATOR = ","
private val NEXT_DATA_REGEX = Regex(
    """<script id="__NEXT_DATA__"[^>]*>(.*?)</script>""",
    RegexOption.DOT_MATCHES_ALL
)

/**
 * Reads public playlist tracks from the Spotify embed page (no Web API credentials).
 * The Web API needs a Premium app owner since Feb 2026; the embed page is unofficial and
 * returns at most 100 tracks, public playlists only.
 * Ktor is suspend-only, so calls are bridged with [runBlocking]; it is confined to this class.
 */
@Component
class SpotifyClient(
    private val client: HttpClient,
    private val gson: Gson,
) {
    fun getPlaylist(spotifyPlaylistId: String): DtoSpotifyPlaylist = runBlocking {
        val html = client.get("$SPOTIFY_EMBED_PLAYLIST_URL/${spotifyPlaylistId.encodeURLPathPart()}") {
            header(HttpHeaders.UserAgent, SPOTIFY_EMBED_USER_AGENT)
        }.bodyOrThrow()

        val entity = html.toEmbedPage().props?.pageProps?.state?.data?.entity
            ?: throw ImportPlaylistException(Status.NOT_FOUND, "Spotify playlist not found")

        val tracks = entity.trackList.orEmpty().mapNotNull { track ->
            val title = track.title?.trim()?.takeIf { it.isNotEmpty() } ?: return@mapNotNull null

            title to track.subtitle.orEmpty()
                .split(SPOTIFY_ARTIST_SEPARATOR)
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }.mapIndexed { index, (title, artists) ->
            DtoSpotifyTrack(
                position = index,
                title = title,
                artists = artists,
                album = null, // not exposed by the embed page
            )
        }

        DebugUtils.d("spotify playlist $spotifyPlaylistId fetched: ${tracks.size} tracks")
        DtoSpotifyPlaylist(
            name = entity.name?.trim()?.takeIf { it.isNotEmpty() },
            tracks = tracks,
        )
    }

    private fun String.toEmbedPage(): SpotifyEmbedPage {
        val json = NEXT_DATA_REGEX.find(this)?.groupValues?.get(1)
            ?: throw ImportPlaylistException(Status.UNAVAILABLE, "Spotify embed data missing")

        return try {
            gson.fromJson(json, SpotifyEmbedPage::class.java)
        } catch (e: JsonParseException) {
            DebugUtils.e("spotify embed parse failed: ${e.message}")
            throw ImportPlaylistException(Status.UNAVAILABLE, "Spotify embed data unreadable")
        }
    }

    private suspend fun HttpResponse.bodyOrThrow(): String {
        if (status.isSuccess()) return bodyAsText()

        DebugUtils.d("spotify request failed: ${status.value} ${request.url}")

        throw when (status) {
            HttpStatusCode.NotFound, HttpStatusCode.BadRequest -> ImportPlaylistException(
                Status.NOT_FOUND, "Spotify playlist not found"
            )

            HttpStatusCode.TooManyRequests -> ImportPlaylistException(
                Status.RESOURCE_EXHAUSTED, "Spotify rate limit reached"
            )

            else -> ImportPlaylistException(Status.UNAVAILABLE, "Spotify unavailable (${status.value})")
        }
    }
}
