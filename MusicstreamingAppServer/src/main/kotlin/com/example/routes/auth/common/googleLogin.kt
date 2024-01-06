package com.example.routes.auth.common

import com.example.data.model.GoogleAuthReq
import com.example.data.model.GoogleUserSession
import com.example.domain.repository.user.GoogleAuthUserRepository
import com.example.util.Constants.ISSUER
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleGoogleLogin(
    googleAuthReq: GoogleAuthReq,
    googleAuthUser: GoogleAuthUserRepository
) {
    val result = googleAuthReq.tokenId.trim().verifyTokenId()

    if (result == null) {
        call.respond(
            message = "invalid request",
            status = HttpStatusCode.Forbidden
        )

        return
    }

    result.let {
        val sub = result.payload["sub"].toString()
        val name = result.payload["name"].toString()
        val pictureUrl = result.payload["picture"].toString()
        val email = result.payload["email"].toString()

        try {
            call.sessions.set(
                GoogleUserSession(
                    sub = sub,
                    name = name
                )
            )

            val userCreationResponse = googleAuthUser.createUser(
                userName = name,
                sub = sub,
                email = email,
                pictureUrl = pictureUrl
            )

            when (userCreationResponse.status) {
                UserCreationStatus.CREATED -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    )
                }

                UserCreationStatus.CONFLICT -> {
                    // todo add
                }

                UserCreationStatus.SOMETHING_WENT_WRONG -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        } catch (e: Exception) {
            call.respond(
                message = "invalid request",
                status = HttpStatusCode.Forbidden
            )
        }
    }
}

private fun String.verifyTokenId(): GoogleIdToken? = try {
    GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(System.getenv("audience")))
        .setIssuer(ISSUER)
        .build()
        .verify(this)
} catch (e: Exception) {
    null
}
