package com.poulastaa.routes.sertup

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.spotify.SpotifyPlaylistResponse
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun Route.getSpotifyPlaylist(
    userService: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetSpotifyPlaylistSong.route) {
            post {
                val user = getUserType() ?: return@post call.respond(
                    message = SpotifyPlaylistResponse(),
                    status = HttpStatusCode.OK
                )

                val playlistId = call.parameters["playlistId"] ?: return@post call.respond(
                    message = SpotifyPlaylistResponse(),
                    status = HttpStatusCode.OK
                )

                // get spotify api response json
                val playlist = getPlaylist(playlistId) ?: return@post call.respond(
                    message = SpotifyPlaylistResponse(),
                    status = HttpStatusCode.OK
                )

                userService.getFoundSpotifySongs(playlist, user).let {
                    call.respond(
                        message = it,
                        status = HttpStatusCode.OK
                    )
                }
            }
        }
    }
}

private suspend fun getPlaylist(playlistId: String): String? {
    val accessToken = getSpotifyPlaylistAccessToken() ?: return null

    val client = HttpClient()

    val result = client.get("https://api.spotify.com/v1/playlists/$playlistId/tracks") {
        header("Authorization", "Bearer $accessToken")
    }

    client.close()
    return result.bodyAsText()
}

@OptIn(InternalAPI::class)
private suspend fun getSpotifyPlaylistAccessToken(
    clientId: String = System.getenv("spotifyClientId"),
    clientSecret: String = System.getenv("spotifyClientSecret")
): String? {
    val tokenEndpoint = "https://accounts.spotify.com/api/token"
    val formData = Parameters.build {
        append("grant_type", "client_credentials")
    }

    val client = HttpClient()
    val response: HttpResponse = client.post(tokenEndpoint) {
        body = FormDataContent(formData)
        headers.append("Authorization", "Basic " + "$clientId:$clientSecret".encodeBase64())
    }

    val responseBody = response.bodyAsText()
    val accessToken = Json.parseToJsonElement(responseBody).jsonObject["access_token"]?.jsonPrimitive?.content

    client.close()

    return accessToken
}

private fun String.encodeBase64(): String {
    return java.util.Base64.getEncoder().encodeToString(this.toByteArray())
}