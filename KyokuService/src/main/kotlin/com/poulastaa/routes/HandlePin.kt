package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.route_model.req.pin.PinReq
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pinData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.PinData.route) {
            put {
                val req =
                    call.receiveNullable<PinReq>() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)
                val payload = call.getReqUserPayload() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)


                val result = service.pinData(req, payload)


                when (result) {
                    true -> call.respond(HttpStatusCode.OK)
                    false -> call.respond(HttpStatusCode.ServiceUnavailable)
                }
            }
        }
    }
}

fun Route.unPinData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.UnPinData.route) {
            put {
                val req =
                    call.receiveNullable<PinReq>() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)
                val payload = call.getReqUserPayload() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.unPinData(req, payload)

                when (result) {
                    true -> call.respond(HttpStatusCode.OK)
                    false -> call.respond(HttpStatusCode.ServiceUnavailable)
                }
            }
        }
    }
}