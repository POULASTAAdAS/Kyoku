package com.poulastaa.routes.auth


import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.jwt.EmailVerificationStatus
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.emailVerificationCheck(userService: UserServiceRepository) {
    route(EndPoints.EmailVerificationCheck.route) {
        get {
            val email = call.parameters["email"]

            if (email == null) {
                call.respondRedirect(EndPoints.UnAuthorised.route)

                return@get
            }

            val result = userService.checkEmailVerification(email)

            when (result.status) {
                EmailVerificationStatus.VERIFIED -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    )
                }

                EmailVerificationStatus.UN_VERIFIED -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    )
                }

                EmailVerificationStatus.USER_NOT_FOUND -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.NotFound
                    )
                }

                EmailVerificationStatus.SOMETHING_WENT_WRONG -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }
    }
}