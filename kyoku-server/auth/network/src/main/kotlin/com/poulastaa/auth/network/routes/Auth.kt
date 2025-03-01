package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.mapper.toAuthResponse
import com.poulastaa.auth.network.mapper.toEmailSignInPayload
import com.poulastaa.auth.network.mapper.toEmailSignUpPayload
import com.poulastaa.auth.network.model.*
import com.poulastaa.auth.network.routes.utils.handleGoogleAuthentication
import com.poulastaa.core.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

fun Route.auth(repo: AuthRepository) {
    route(EndPoints.Auth.route) {
        post {
            val reqString = call.receiveText()
            val jsonObj = Json.parseToJsonElement(reqString).jsonObject

            when {
                jsonObj.containsKey("token") -> {
                    val payload = Json.decodeFromString<GoogleAuthRequest>(reqString)
                    handleGoogleAuthentication(payload, repo)
                }

                jsonObj.containsKey("email") && jsonObj.containsKey("username") -> {
                    val payload = Json.decodeFromString<EmailSignUpRequest>(reqString)
                    val response = repo.emailSignUp(payload.toEmailSignUpPayload()).toAuthResponse()

                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }

                jsonObj.containsKey("password") -> {
                    val payload = Json.decodeFromString<EmailLogInRequest>(reqString)
                    val response = repo.emailLogIn(payload.toEmailSignInPayload()).toAuthResponse()

                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }

                else -> return@post call.respond(
                    status = HttpStatusCode.OK,
                    message = AuthenticationResponse(
                        status = AuthStatusResponse.SERVER_ERROR,
                    )
                )
            }
        }
    }
}