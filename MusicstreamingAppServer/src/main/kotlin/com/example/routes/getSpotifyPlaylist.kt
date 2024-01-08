package com.example.routes

import com.example.data.model.EndPoints
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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun Route.getSpotifyPlaylist(
    clientId: String = System.getenv("spotifyClientId"),
    clientSecret: String = System.getenv("spotifyClientSecret"),
) {
    authenticate("jwt-auth") {
        route(EndPoints.GetSpotifyPlaylist.route) {
            get { // todo it's a mess
                // todo get playlistID form url
                // todo val spotifyUrl = call.parameters["url"]

                val playlistId = "4k9rnYaAavL1Q0sTsgvUA4"
                val accessToken = runBlocking {
                    getAccessToken(clientId, clientSecret)
                }


                val client = HttpClient()

                val result = runBlocking {
                    client.get("https://api.spotify.com/v1/playlists/$playlistId/tracks") {
                        header("Authorization", "Bearer $accessToken")
                    }.bodyAsText()
                }

                client.close()

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

@OptIn(InternalAPI::class)
suspend fun getAccessToken(
    clientId: String,
    clientSecret: String
): String {
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

    return accessToken ?: throw IllegalStateException("Access token not found in the response")
}

private fun String.encodeBase64(): String {
    return java.util.Base64.getEncoder().encodeToString(this.toByteArray())
}
