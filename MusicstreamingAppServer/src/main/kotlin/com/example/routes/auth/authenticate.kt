package com.example.routes.auth

import com.example.data.model.*
import com.example.domain.repository.user.EmailAuthUserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.createAuthRoute(emailAuthUser: EmailAuthUserRepository) {
    route(EndPoints.Auth.route) {
        post {
            val message = call.receiveText()

            val payload = try {
                Json.decodeFromString<DefaultAuthModel>(message)
            } catch (e: Exception) {
                call.respond(
                    message = "invalid request",
                    status = HttpStatusCode.Unauthorized
                )

                return@post
            }

            when (payload) {
                is EmailLoginReq -> {
                    call.respond(message = "EmailLoginReq")
                }

                is EmailSignUpReq -> {
                    handleEmailSignup(
                        emailSignUpReq = payload,
                        emailAuthUser = emailAuthUser
                    )
                }

                is GoogleAuthReq -> {
                    call.respond(message = "GoogleAuthReq")
                }
            }
        }
    }
}