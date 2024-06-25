package com.poulastaa.routes.forgot_password

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sendForgotPasswordLink(
    service: ServiceRepository,
) {
    route(EndPoints.ForgotPassword.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(EndPoints.UnAuthorised.route)

            val response = service.sendForgotPasswordMail(email)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}