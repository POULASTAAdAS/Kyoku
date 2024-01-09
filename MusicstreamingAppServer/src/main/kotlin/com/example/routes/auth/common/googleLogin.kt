package com.example.routes.auth.common

import com.example.data.model.EndPoints
import com.example.data.model.auth.GoogleAuthReq
import com.example.data.model.auth.UserCreationStatus
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import com.example.util.Constants.ISSUER
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
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


    val sub = result.payload["sub"].toString()
    val name = result.payload["name"].toString()
    val pictureUrl = result.payload["picture"].toString()
    val email = result.payload["email"].toString()

    try {
//            call.sessions.set(
//                GoogleUserSession(
//                    sub = sub,
//                    name = name
//                )
//            )

        val userCreationResponse = googleAuthUser.createUser(
            userName = name,
            sub = sub,
            email = email,
            pictureUrl = pictureUrl
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
