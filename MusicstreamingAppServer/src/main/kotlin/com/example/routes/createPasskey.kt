package com.example.routes

import com.example.data.model.EndPoints
import com.example.data.model.auth.req.PasskeyReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPasskey() {
    route(EndPoints.Passkey.route) {
        get {
            call.respond(
                message = PasskeyReq(),
                status = HttpStatusCode.OK
            )
        }
    }
}