package com.example.routes.auth

import com.example.data.model.EndPoints
import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.routes.auth.common.EmailVerificationStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.emailVerificationCheck(emailAuthUser: EmailAuthUserRepository) {
    route(EndPoints.EmailVerificationCheck.route) {
        get {
            val email = call.parameters["email"]

            if (email == null) {
                call.respond(
                    message = "invalid request",
                    status = HttpStatusCode.Forbidden
                )

                return@get
            }

            email.let {
                when (
                    val message = emailAuthUser.checkEmailVerification(it)
                ) {
                    EmailVerificationStatus.VERIFIED -> {
                        call.respond(
                            message = message,
                            status = HttpStatusCode.OK
                        )
                    }

                    EmailVerificationStatus.UN_VERIFIED -> {
                        call.respond(
                            message = message,
                            status = HttpStatusCode.OK
                        )
                    }

                    EmailVerificationStatus.SOMETHING_WENT_WRONG -> {
                        call.respond(
                            message = message,
                            status = HttpStatusCode.InternalServerError
                        )
                    }
                }
            }
        }
    }
}