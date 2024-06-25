package com.poulastaa.routes.verify_mail

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUpEmailVerificationCheck(
    service: ServiceRepository,
) {
    route(EndPoints.SignUpEmailVerificationCheck.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(EndPoints.UnAuthorised.route)

            val response = service.signUpEmailVerificationCheck(email)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}