package com.poulastaa.routes.setup

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.SpotifySongTitle
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.*

fun Route.getSpotifyPlaylist(

) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetSpotifyPlaylistSong.route) {
            post {

            }
        }
    }
}


private fun getSpotifySongPayload(json: String): List<SpotifySongTitle> {
    val list = ArrayList<SpotifySongTitle>()

    return try {
        val jsonElement = Json.parseToJsonElement(json)

        val itemsArray = jsonElement.jsonObject["items"]?.jsonArray

        itemsArray?.forEach { item ->
            val trackJson = item?.jsonObject?.get("track") // some items don't exist this check is important

            if (trackJson != null && trackJson is JsonObject) {
                trackJson.jsonObject["track"]?.jsonObject?.get("name")
                    ?.jsonPrimitive?.contentOrNull?.let { name ->
                        if (name.isNotBlank()) {
                            val title = name.removeAlbumNameIfAny()

                            if (title.isNotEmpty()) list.add(title)
                        }
                    }
            }
        }
        list
    } catch (_: Exception) {
        emptyList()
    }
}


fun String.removeAlbumNameIfAny(): String =
    this.replace(Regex("\\(.*"), "").trim()

fun String.getAlbum(): String {
    val temp = Regex("\"([^\"]+)\"").find(this)

    temp?.let {
        return it.groupValues[1].trim()
    }
    return this.replace(Regex("\\(.*"), "").trim()
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