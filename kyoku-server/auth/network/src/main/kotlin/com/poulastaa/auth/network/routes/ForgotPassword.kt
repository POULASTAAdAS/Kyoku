package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.mapper.toForgotPasswordResponse
import com.poulastaa.auth.network.model.ForgotPasswordResponse
import com.poulastaa.auth.network.model.ForgotPasswordResponseStatus
import com.poulastaa.core.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.forgotPassword(
    repo: AuthRepository,
) {
    route(EndPoints.ForgotPassword.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(
                status = HttpStatusCode.OK,
                message = ForgotPasswordResponse(ForgotPasswordResponseStatus.EMAIL_NOT_PROVIDED)
            )

            val result = repo.forgotPassword(email)

            call.respond(
                status = HttpStatusCode.OK,
                message = result.toForgotPasswordResponse()
            )
        }
    }
}