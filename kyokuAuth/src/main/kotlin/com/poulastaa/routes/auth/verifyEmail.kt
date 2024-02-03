package com.poulastaa.routes.auth

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.stat.UpdateEmailVerificationStatus
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.verifyEmail(userService: UserServiceRepository) {
    route(EndPoints.VerifyEmail.route) {
        get {
            val token = call.parameters["token"]

            if (token == null) {
                call.respond(
                    message = "invalid request",
                    status = HttpStatusCode.Forbidden
                )

                return@get
            }

            when (
                val status = userService.updateVerificationStatus(
                    token = token
                )
            ) {
                UpdateEmailVerificationStatus.VERIFIED -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.OK
                    )
                }

                UpdateEmailVerificationStatus.TOKEN_USED -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.BadRequest
                    )
                }

                UpdateEmailVerificationStatus.USER_NOT_FOUND -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.NotFound
                    )
                }

                UpdateEmailVerificationStatus.TOKEN_NOT_VALID -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.Forbidden
                    )
                }

                UpdateEmailVerificationStatus.SOMETHING_WENT_WRONG -> {
                    call.respond(
                        message = status,
                        status = HttpStatusCode.Forbidden
                    )
                }
            }
        }
    }
}