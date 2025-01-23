package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.auth.network.mapper.toJWTResponse
import com.poulastaa.auth.network.model.JwtTokenResponse
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getJWTToken(
    auth: AuthRepository,
) {
    route(EndPoints.GetJWTToken.route) {
        get {
            val email = call.parameters["email"] ?: return@get call.respond(
                message = JwtTokenResponse(),
                status = HttpStatusCode.OK
            )

            val result = auth.getJWTToken(email)

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