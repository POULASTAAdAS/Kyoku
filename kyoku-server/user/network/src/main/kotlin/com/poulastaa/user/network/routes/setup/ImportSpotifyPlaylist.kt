package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.core.network.mapper.toResponsePlaylistFull
import com.poulastaa.core.network.model.ResponseFullPlaylist
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.domain.repository.SpotifySongTitle
import com.poulastaa.user.network.model.ImportSpotifyPlaylistReq
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.*
import java.util.*

fun Route.importSpotifyPlaylist(
    clientId: String,
    clientSecret: String,
    repo: SetupRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(path = EndPoints.ImportSpotifyPlaylist.route) {
            post {
                val playlistId = call.receiveNullable<ImportSpotifyPlaylistReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val payload = call.getReqUserPayload()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val playlistJson = getPlaylist(playlistId.playlistId, clientId, clientSecret)
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val spotifyPayload = getSpotifySongPayload(playlistJson)

                if (spotifyPayload.isEmpty()) return@post call.respond(
                    message = ResponseFullPlaylist(),
                    status = HttpStatusCode.OK
                )

                val playlist = repo.getSpotifyPlaylist(payload, spotifyPayload)
                    ?: return@post call.respond(
                        message = ResponseFullPlaylist(),
                        status = HttpStatusCode.NotFound
                    )

                call.respond(
                    message = playlist.toResponsePlaylistFull(),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

private suspend fun getPlaylist(
    playlistId: String,
    clientId: String,
    clientSecret: String,
): String? {
    val accessToken = getSpotifyPlaylistAccessToken(clientId, clientSecret) ?: return null

    val client = HttpClient()

    val result = client.get("https://api.spotify.com/v1/playlists/$playlistId/tracks") {
        header("Authorization", "Bearer $accessToken")
    }

    client.close()
    return result.bodyAsText()
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
    } catch (_: Exception) {
        emptyList()
    }
}

@OptIn(InternalAPI::class)
private suspend fun getSpotifyPlaylistAccessToken(
    clientId: String,
    clientSecret: String,
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
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

private fun String.removeAlbumNameIfAny(): String =
    this.replace(Regex("\\(.*"), "").trim()