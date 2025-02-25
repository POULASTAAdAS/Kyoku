package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.mapper.toJWTResponse
import com.poulastaa.auth.network.model.RefreshTokenRequest
import com.poulastaa.core.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getRefreshToken(repo: AuthRepository) {
    route(EndPoints.RefreshToken.route) {
        post {
            val req = call.receiveNullable<RefreshTokenRequest>()
                ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

            val result = repo.refreshJWTToken(req.token, req.email)
                ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

            call.respond(
                message = result.toJWTResponse(),
                status = HttpStatusCode.OK
            )
        }
    }
}