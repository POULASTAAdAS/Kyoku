package com.example.routes.auth

import com.example.data.model.EndPoints
import com.example.data.model.auth.stat.SendVerificationMailStatus
import com.example.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.forgotPassword(
    userService: UserServiceRepository,
) {
    route(EndPoints.ForgotPassword.route) {
        get {
            val email = call.parameters["email"]

            if (email == null) {
                call.respondRedirect(EndPoints.UnAuthorised.route)

                return@get
            }

            val result = userService.sendForgotPasswordMail(email = email.trim())

            when (result.status) {
                SendVerificationMailStatus.USER_EXISTS -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    )
                }

                SendVerificationMailStatus.USER_NOT_FOUND -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.Forbidden
                    )
                }

                SendVerificationMailStatus.SOMETHING_WENT_WRONG -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.Forbidden
                    )
                }
            }
        }
    }
}