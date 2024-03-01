package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.auth.UnAuthorisedResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unauthorized() {
    route(EndPoints.UnAuthorised.route) {
        get {
            call.respond(
                message = UnAuthorisedResponse(
                    message = "Unauthorised"
                ),
                status = HttpStatusCode.Forbidden
            )
        }
    }
}