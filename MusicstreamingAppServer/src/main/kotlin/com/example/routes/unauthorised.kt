package com.example.routes

import com.example.data.model.EndPoints
import com.example.data.model.api.UnAuthorisedResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unauthorised() {
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