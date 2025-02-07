package com.poulastaa.auth.network.routes

import com.poulastaa.auth.network.model.UnAuthorizedResponse
import com.poulastaa.core.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unAuthorized() {
    route(EndPoints.UnAuthorized.route) {
        get {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = UnAuthorizedResponse()
            )
        }
    }
}