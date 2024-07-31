package com.poulastaa.routes.auth.components

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.poulastaa.data.model.auth.req.GoogleAuthReq
import com.poulastaa.data.model.auth.response.Payload
import com.poulastaa.data.model.auth.response.UserAuthRes
import com.poulastaa.data.model.auth.response.UserAuthStatus
import com.poulastaa.data.model.session.UserSession
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleGoogleAuth(
    service: ServiceRepository,
    req: GoogleAuthReq,
) {
    val token = req.verifyTokenId() ?: return call.respond(
        message = UserAuthRes(
            status = UserAuthStatus.TOKEN_NOT_VALID
        ),
        status = HttpStatusCode.Unauthorized
    )

    val payload = try {
        token.toPayload()
    } catch (_: Exception) {
        return call.respond(
            message = UserAuthRes(
                status = UserAuthStatus.SOMETHING_WENT_WRONG,
            ),
            status = HttpStatusCode.BadRequest
        )
    }

    val response = service.googleAuth(
        payload = payload,
        countryCode = req.countryCode
    )

    when (response.first.status) {
        UserAuthStatus.CREATED,
        UserAuthStatus.USER_FOUND_HOME,
        UserAuthStatus.USER_FOUND_SET_GENRE,
        UserAuthStatus.USER_FOUND_STORE_B_DATE,
        UserAuthStatus.USER_FOUND_SET_ARTIST,
        -> {
            setSession(
                call = call,
                userId = response.second,
                email = payload.email
            )

            call.respond(
                message = response.first,
                status = HttpStatusCode.OK
            )
        }

        else -> {
            call.respond(
                message = response.first,
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}

private fun setSession(
    call: ApplicationCall,
    userId: Long,
    email: String,
) {
    call.sessions.set(
        UserSession(
            userId = userId,
            email = email
        )
    )
}

private fun GoogleAuthReq.verifyTokenId(): GoogleIdToken? = try {
    GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(System.getenv("clientId")))
        .setIssuer(System.getenv("ISSUER"))
        .build()
        .verify(this.token)
} catch (e: Exception) {
    null
}

private fun GoogleIdToken.toPayload() = Payload(
    sub = this.payload["sub"].toString(),
    userName = this.payload["name"].toString(),
    email = this.payload["email"].toString(),
    pictureUrl = this.payload["picture"].toString()
)