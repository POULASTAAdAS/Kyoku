package com.poulastaa.routes.auth

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.req.RefreshTokenReq
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.refreshToken(
    userService: UserServiceRepository
) {
    route(EndPoints.RefreshToken.route) {
        post {
            val req = call.receiveNullable<RefreshTokenReq>()
                ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

            val response = userService.refreshToken(req.oldRefreshToken)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}