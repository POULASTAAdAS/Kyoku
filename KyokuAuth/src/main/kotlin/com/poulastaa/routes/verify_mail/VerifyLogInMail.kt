package com.poulastaa.routes.verify_mail

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.routes.verify_mail.components.verificationResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.verifyLogInMail(
    service: ServiceRepository,
) {
    route(EndPoints.VerifyLogInEmail.route) {
        get {
            val token = call.parameters["token"] ?: return@get call.respondText(EndPoints.UnAuthorised.route)

            val status = service.updateLogInEmailVerificationStatus(token)

            verificationResponse(
                call = call,
                status = status,
            )
        }
    }
}