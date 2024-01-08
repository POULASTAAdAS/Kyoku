package com.example.routes.auth

import com.example.data.model.EndPoints
import com.example.data.model.auth.AuthReqBaseModel
import com.example.data.model.auth.EmailLoginReq
import com.example.data.model.auth.EmailSignUpReq
import com.example.data.model.auth.GoogleAuthReq
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import com.example.routes.auth.common.handleEmailLogin
import com.example.routes.auth.common.handleEmailSignup
import com.example.routes.auth.common.handleGoogleLogin
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.createAuthRoute(
    emailAuthUser: EmailAuthUserRepository,
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
                is EmailLoginReq -> {
                    handleEmailLogin(
                        emailLoginReq = payload,
                        emailAuthUser = emailAuthUser
                    )
                }

                is EmailSignUpReq -> {
                    handleEmailSignup(
                        emailSignUpReq = payload,
                        emailAuthUser = emailAuthUser
                    )
                }

                is GoogleAuthReq -> {
                    handleGoogleLogin(
                        googleAuthReq = payload,
                        googleAuthUser = googleAuthUser
                    )
                }
            }
        }
    }
}