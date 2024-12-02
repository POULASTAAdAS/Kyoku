package com.pouluastaa.auth.network.routes

import com.poulastaa.core.domain.model.Endpoints
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.auth() {
    route(Endpoints.Auth.route) {
        post {
            val req = call.receiveText()

            call.respond(
                message = "Test",
                status = HttpStatusCode.OK
            )
        }
    }
}