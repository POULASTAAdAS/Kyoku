package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.core.domain.model.Endpoints
import com.poulastaa.auth.network.mapper.toJWTResponse
import com.poulastaa.auth.network.model.JwtTokenResponse
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.verifyEmailVerificationState(
    auth: AuthRepository,
) {
    route(Endpoints.VerifyEmailVerificationState.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(
                message = JwtTokenResponse(),
                status = HttpStatusCode.OK
            )

            val result = auth.checkEmailVerificationState(email)

            if (result != null) call.respond(
                status = HttpStatusCode.OK,
                message = result.toJWTResponse()
            ) else call.respond(
                status = HttpStatusCode.OK,
                message = JwtTokenResponse()
            )
        }
    }
}