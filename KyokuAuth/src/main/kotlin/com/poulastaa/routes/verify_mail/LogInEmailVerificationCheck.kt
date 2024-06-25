package com.poulastaa.routes.verify_mail

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.logInEmailVerificationCheck(
    service: ServiceRepository,
) {
    route(EndPoints.LogInEmailVerificationCheck.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(EndPoints.UnAuthorised.route)

            val response = service.logInEmailVerificationCheck(email)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}