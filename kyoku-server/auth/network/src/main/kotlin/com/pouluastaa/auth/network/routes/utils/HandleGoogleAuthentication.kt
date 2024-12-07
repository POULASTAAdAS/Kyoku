package com.pouluastaa.auth.network.routes.utils

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.domain.model.GoogleAuthPayloadDto
import com.pouluastaa.auth.network.mapper.toAuthResponse
import com.pouluastaa.auth.network.model.AuthenticationResponse
import com.pouluastaa.auth.network.model.AuthStatusResponse
import com.pouluastaa.auth.network.model.GoogleAuthRequest
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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

    val response = repo.googleAuth(payload, req.countryCode).toAuthResponse()

    call.respond(
        status = HttpStatusCode.OK,
        message = response
    )
}


private fun GoogleAuthRequest.verifyTokenId(): GoogleIdToken? = try {
    GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(System.getenv("clientId")))
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