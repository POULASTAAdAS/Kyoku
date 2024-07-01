package com.poulastaa.routes.setup

import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.data.model.req.setup.CreatePlaylistReq
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.SpotifySongTitle
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.*

fun Route.getSpotifyPlaylist(
    service: ServiceRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetSpotifyPlaylistSong.route) {
            post {
                val playlistId = call.receiveNullable<CreatePlaylistReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val userPayload = call.getReqUserPayload()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val playlistJson =
                    getPlaylist(playlistId.playlistId) ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val spotifyPayload = getSpotifySongPayload(playlistJson)

                if (spotifyPayload.isEmpty()) return@post call.respond(
                    message = PlaylistDto(),
                    status = HttpStatusCode.OK
                )

                val response = service.getSpotifyPlaylist(
                    userPayload = userPayload,
                    spotifyPayload = spotifyPayload
                )

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
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
    clientSecret: String = System.getenv("spotifyClientSecret"),
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

private fun getSpotifySongPayload(json: String): List<SpotifySongTitle> {
    val list = ArrayList<SpotifySongTitle>()

    return try {
        val jsonElement = Json.parseToJsonElement(json)

        val itemsArray = jsonElement.jsonObject["items"]?.jsonArray

        itemsArray?.forEach { item ->
            item?.jsonObject?.get("track")?.jsonObject?.get("name")?.jsonPrimitive?.contentOrNull?.let { name ->
                if (name.isNotBlank()) {
                    val title = name.removeAlbumNameIfAny()

                    if (title.isNotEmpty()) list.add(title)
                }
            }
        }
        list
    } catch (e: Exception) {
        emptyList()
    }
}

private fun String.removeAlbumNameIfAny(): String =
    this.replace(Regex("\\(.*"), "").trim()