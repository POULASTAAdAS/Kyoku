package com.poulastaa.routes.auth


import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.jwt.SendForgotPasswordMail
import com.poulastaa.data.model.auth.jwt.SendForgotPasswordMailStatus
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.forgotPassword(
    userService: UserServiceRepository,
) {
    route(EndPoints.ForgotPassword.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(
                message = SendForgotPasswordMail(
                    status = SendForgotPasswordMailStatus.SOMETHING_WENT_WRONG
                ),
                status = HttpStatusCode.OK
            )

            val result = userService.sendForgotPasswordMail(email = email.trim())

            when (result.status) {
                SendForgotPasswordMailStatus.USER_EXISTS -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    )
                }

                SendForgotPasswordMailStatus.USER_NOT_FOUND -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    )
                }

                SendForgotPasswordMailStatus.SOMETHING_WENT_WRONG -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    )
                }
            }
        }
    }
}