package com.example.routes.auth

import com.example.data.model.EndPoints
import com.example.data.model.auth.req.AuthReqBaseModel
import com.example.data.model.auth.req.EmailLoginReq
import com.example.data.model.auth.req.EmailSignUpReq
import com.example.data.model.auth.req.GoogleAuthReq
import com.example.domain.repository.UserServiceRepository
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import com.example.routes.auth.common.handleEmailLogin
import com.example.routes.auth.common.handleEmailSignup
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.authRoute(
    userService: UserServiceRepository,
    googleAuthUser: GoogleAuthUserRepository
) {
    route(EndPoints.Auth.route) {
        post {
            val message = call.receiveText()

            val payload = try {
                Json.decodeFromString<AuthReqBaseModel>(message)
            } catch (e: Exception) {
                call.respondRedirect(EndPoints.UnAuthorised.route)

                return@post
            }

            when (payload) {
                is EmailSignUpReq -> {
                    handleEmailSignup(
                        emailSignUpReq = payload,
                        userService = userService,
                        response = { emailSignInResponse, httpStatusCode ->
                            call.respond(
                                message = emailSignInResponse,
                                status = httpStatusCode
                            )
                        }
                    )
                }

                is EmailLoginReq -> {
                    handleEmailLogin(
                        emailLoginReq = payload,
                        userService = userService,
                    )
                }

                is GoogleAuthReq -> {
//                    handleGoogleLogin(
//                        googleAuthReq = payload,
//                        googleAuthUser = googleAuthUser
//                    )
                }
            }
        }
    }
}