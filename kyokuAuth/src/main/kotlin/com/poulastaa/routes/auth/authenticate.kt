package com.poulastaa.routes.auth


import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.req.AuthReqBaseModel
import com.poulastaa.data.model.auth.req.EmailLoginReq
import com.poulastaa.data.model.auth.req.EmailSignUpReq
import com.poulastaa.data.model.auth.req.GoogleAuthReq
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.routes.auth.common.handleEmailLogin
import com.poulastaa.routes.auth.common.handleEmailSignup
import com.poulastaa.routes.auth.common.handleGoogleLogin
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.authRoute(
    userService: UserServiceRepository
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
                    handleGoogleLogin(
                        googleAuthReq = payload,
                        userService = userService
                    )
                }
            }
        }
    }
}