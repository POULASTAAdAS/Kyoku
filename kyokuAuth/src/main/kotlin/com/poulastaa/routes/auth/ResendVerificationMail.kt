package com.poulastaa.routes.auth

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.jwt.ResendVerificationMailResponse
import com.poulastaa.data.model.auth.jwt.ResendVerificationMailStatus
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.verifyEmailIdWithApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.resendVerificationMail(
    userService: UserServiceRepository
) {
    route(EndPoints.ResendVerificationMail.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(
                message = ResendVerificationMailResponse(
                    status = ResendVerificationMailStatus.SOMETHING_WENT_WRONG
                ),
                status = HttpStatusCode.OK
            )

            if (false)
                verifyEmailIdWithApi(email).let {
                    if (!it) return@get call.respond(
                        message = ResendVerificationMailResponse(
                            status = ResendVerificationMailStatus.NOT_A_VALID_EMAIL
                        ),
                        status = HttpStatusCode.OK
                    )
                }

            val response = userService.sendVerificationMail(email)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}