package com.poulastaa.kyoku.playlist.service

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.poulastaa.kyoku.playlist.domain.model.DtoSpotifyPlaylist
import com.poulastaa.kyoku.playlist.domain.model.DtoSpotifyTrack
import com.poulastaa.kyoku.playlist.domain.model.ImportPlaylistException
import com.poulastaa.kyoku.playlist.domain.model.internal.PlaylistResponse
import com.poulastaa.kyoku.playlist.domain.model.internal.SpotifyToken
import com.poulastaa.kyoku.playlist.utils.DebugUtils
import io.grpc.Status
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

private const val SPOTIFY_ACCESS_TOKEN_URL = "https://accounts.spotify.com/api/token"
private const val SPOTIFY_API_URL = "https://api.spotify.com/v1"
private const val SPOTIFY_ACCESS_TOKEN_PARAM_KEY = "grant_type"
private const val SPOTIFY_ACCESS_TOKEN_PARAM_VALUE = "client_credentials"
private const val SPOTIFY_TRACKS_PAGE_LIMIT = 100
private const val SPOTIFY_TRACK_FIELDS = "next,items(track(name,album(name),artists(name)))"
private const val TOKEN_EXPIRY_MARGIN_SECONDS = 60L

/**
 * Blocking Spotify Web API client (client-credentials flow).
 * Ktor is suspend-only, so calls are bridged with [runBlocking]; it is confined to this class.
 */
@Component
class SpotifyClient(
    private val client: HttpClient,
    private val gson: Gson,
    @param:Value("\${spotify.clientId}")
    private val clientId: String,
    @param:Value("\${spotify.clientSecret}")
    private val clientSecret: String,
) {
    @Volatile
    private var cachedToken: Pair<String, Instant>? = null

    fun getPlaylist(spotifyPlaylistId: String): DtoSpotifyPlaylist = runBlocking {
        val token = getAccessToken()
        val playlistId = spotifyPlaylistId.encodeURLPathPart()

        val name = get("$SPOTIFY_API_URL/playlists/$playlistId?fields=name", token)
            .let { gson.fromJson(it, JsonObject::class.java) }
            .get("name")?.takeUnless { it.isJsonNull }?.asString
            ?.trim()?.takeIf { it.isNotEmpty() }

        val tracks = mutableListOf<DtoSpotifyTrack>()
        var nextUrl: String? = URLBuilder("$SPOTIFY_API_URL/playlists/$playlistId/tracks").apply {
            parameters.append("limit", SPOTIFY_TRACKS_PAGE_LIMIT.toString())
            parameters.append("fields", SPOTIFY_TRACK_FIELDS)
        }.buildString()

        while (nextUrl != null) {
            val page = gson.fromJson(get(nextUrl, token), PlaylistResponse::class.java)

            page.items.orEmpty().forEach { item ->
                val track = item.track ?: return@forEach // removed / local tracks
                val title = track.name?.takeIf { it.isNotBlank() } ?: return@forEach

                tracks += DtoSpotifyTrack(
                    position = tracks.size,
                    title = title,
                    artists = track.artists.orEmpty().mapNotNull { it.name?.takeIf { n -> n.isNotBlank() } },
                    album = track.album?.name?.takeIf { it.isNotBlank() }
                )
            }

            nextUrl = page.next
        }

        DebugUtils.d("spotify playlist $spotifyPlaylistId fetched: ${tracks.size} tracks")
        DtoSpotifyPlaylist(name = name, tracks = tracks)
    }

    private suspend fun get(url: String, token: String): String {
        val response = client.get(url) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return response.bodyOrThrow()
    }

    private suspend fun getAccessToken(): String {
        cachedToken?.let { (token, expiresAt) ->
            if (Instant.now().isBefore(expiresAt)) return token
        }

        val response = client.post(SPOTIFY_ACCESS_TOKEN_URL) {
            setBody(
                FormDataContent(Parameters.build {
                    append(SPOTIFY_ACCESS_TOKEN_PARAM_KEY, SPOTIFY_ACCESS_TOKEN_PARAM_VALUE)
                })
            )
            header(HttpHeaders.Authorization, "Basic " + "$clientId:$clientSecret".encodeBase64())
        }

        val token = gson.fromJson(response.bodyOrThrow(), SpotifyToken::class.java)
        val accessToken = token.accessToken ?: throw ImportPlaylistException(
            Status.UNAVAILABLE, "Spotify access token missing"
        )

        cachedToken = accessToken to Instant.now()
            .plusSeconds((token.expiresIn - TOKEN_EXPIRY_MARGIN_SECONDS).coerceAtLeast(0))

        return accessToken
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

            HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> {
                cachedToken = null
                ImportPlaylistException(Status.UNAVAILABLE, "Spotify authorization failed")
            }

            else -> ImportPlaylistException(Status.UNAVAILABLE, "Spotify unavailable (${status.value})")
        }
    }

    private fun String.encodeBase64() = Base64.getEncoder().encodeToString(this.toByteArray())
}
