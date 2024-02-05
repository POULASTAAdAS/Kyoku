package com.poulastaa.routes

import com.poulastaa.data.model.EndPoints
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unauthorized() {
    route(EndPoints.UnAuthorised.route) {
        get {
            call.respond(
                message = "invalid request",
                status = HttpStatusCode.Unauthorized
            )
        }
    }
}