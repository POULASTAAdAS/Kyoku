package com.example.routes.auth.common

import com.example.data.model.EndPoints
import com.example.data.model.GoogleUserSession
import com.example.data.model.auth.req.GoogleAuthReq
import com.example.data.model.auth.stat.UserCreationStatus
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import com.example.util.Constants.ISSUER
import com.example.util.toPayload
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
    val result = googleAuthReq.tokenId.verifyTokenId()

    if (result == null) {
        call.respondRedirect(EndPoints.UnAuthorised.route)

        return
    }

    try {
        val payload = result.toPayload()

        call.sessions.set(
            GoogleUserSession(
                sub = payload.sub,
                name = payload.userName
            )
        )

        val userCreationResponse = googleAuthUser.createUser(
            userName = payload.userName,
            sub = payload.sub,
            email = payload.email,
            pictureUrl = payload.pictureUrl
        )

        when (userCreationResponse.status) {
            UserCreationStatus.CREATED -> {
                call.respond(
                    message = userCreationResponse,
                    status = HttpStatusCode.OK
                )
            }

            UserCreationStatus.CONFLICT -> {
                call.respond(
                    message = userCreationResponse,
                    status = HttpStatusCode.OK
                )
            }

            UserCreationStatus.SOMETHING_WENT_WRONG -> {
                call.respond(
                    message = userCreationResponse,
                    status = HttpStatusCode.InternalServerError
                )
            }

            UserCreationStatus.EMAIL_NOT_VALID -> Unit // this will not occur
        }
    } catch (e: Exception) {
        e.printStackTrace()
        call.respondRedirect(EndPoints.UnAuthorised.route)
    }
}

private fun String.verifyTokenId(): GoogleIdToken? = try {
    println(this)
    GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(System.getenv("clientId")))
        .setIssuer(ISSUER)
        .build()
        .verify(this)
} catch (e: Exception) {
    null
}
