package com.poulastaa.routes.auth

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.stat.PasswordResetStatus
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.resetPassword(
    userService: UserServiceRepository,
) {
    route(EndPoints.ResetPassword.route) {
        get {
            val token = call.parameters["token"]
            val password = call.parameters["password"]


            if (password == null || token == null) {
                call.respondRedirect(EndPoints.UnAuthorised.route)

                return@get
            }

            when (val status = userService.resetPassword(token, password)) {
                PasswordResetStatus.SUCCESSFUL -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.OK
                    )
                }

                PasswordResetStatus.SAME_AS_OLD_PASSWORD -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.OK
                    )
                }

                PasswordResetStatus.USER_NOT_FOUND -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.Forbidden
                    )
                }

                PasswordResetStatus.TOKEN_EXPIRED -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.BadRequest
                    )
                }

                PasswordResetStatus.SOMETHING_WENT_WRONG -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.Forbidden
                    )
                }

                PasswordResetStatus.TOKEN_USED -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.Forbidden
                    )
                }
            }
        }
    }
}