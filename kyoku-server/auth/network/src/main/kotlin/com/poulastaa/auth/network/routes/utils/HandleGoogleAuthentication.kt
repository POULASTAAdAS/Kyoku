package com.poulastaa.auth.network.routes.utils

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.poulastaa.auth.domain.model.AuthResponseStatusDto
import com.poulastaa.auth.domain.model.GoogleAuthPayloadDto
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.mapper.toAuthResponse
import com.poulastaa.auth.network.model.AuthStatusResponse
import com.poulastaa.auth.network.model.AuthenticationResponse
import com.poulastaa.auth.network.model.GoogleAuthRequest
import com.poulastaa.core.network.model.UserSession
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

suspend fun RoutingContext.handleGoogleAuthentication(
    req: GoogleAuthRequest,
    repo: AuthRepository,
) {
    val token = req.verifyTokenId() ?: return call.respond(
        status = HttpStatusCode.OK,
        message = AuthenticationResponse(
            status = AuthStatusResponse.TOKEN_EXPIRED,
        )
    )

    val payload = try {
        token.toGooglePayload()
    } catch (_: Exception) {
        return call.respond(
            status = HttpStatusCode.OK,
            message = AuthenticationResponse(
                status = AuthStatusResponse.SERVER_ERROR,
            )
        )
    }

    val response = repo.googleAuth(payload, req.countryCode)

    if (response.status == AuthResponseStatusDto.USER_CREATED ||
        response.status == AuthResponseStatusDto.USER_FOUND_HOME ||
        response.status == AuthResponseStatusDto.USER_FOUND_SET_GENRE ||
        response.status == AuthResponseStatusDto.USER_FOUND_STORE_B_DATE ||
        response.status == AuthResponseStatusDto.USER_FOUND_SET_ARTIST
    ) call.sessions.set<UserSession>(
        UserSession(
            userId = response.user.id,
            email = response.user.email
        )
    )

    call.respond(
        status = HttpStatusCode.OK,
        message = response.toAuthResponse()
    )
}

private fun GoogleAuthRequest.verifyTokenId(): GoogleIdToken? = try {
    GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(System.getenv("CLIENT_ID")))
        .setIssuer(System.getenv("ISSUER"))
        .build()
        .verify(this.token)
} catch (_: Exception) {
    null
}

private fun GoogleIdToken.toGooglePayload() = GoogleAuthPayloadDto(
    sub = this.payload["sub"].toString(),
    userName = this.payload["name"].toString(),
    email = this.payload["email"].toString(),
    profilePicUrl = this.payload["picture"].toString()
)