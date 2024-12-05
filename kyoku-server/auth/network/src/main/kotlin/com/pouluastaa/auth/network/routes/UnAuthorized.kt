package com.pouluastaa.auth.network.routes

import com.poulastaa.core.domain.model.Endpoints
import com.pouluastaa.auth.network.model.UnAuthorizedResponse
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unAuthorized() {
    route(Endpoints.UnAuthorized.route) {
        get {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = UnAuthorizedResponse()
            )
        }
    }
}