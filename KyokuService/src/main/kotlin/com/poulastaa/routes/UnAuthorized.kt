package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unAuthorized() {
    route(EndPoints.UnAuthorised.route) {
        get {
            call.respond(
                message = "Fuck you.Stop westing your and my time.",
                status = HttpStatusCode.Unauthorized
            )
        }
    }
}