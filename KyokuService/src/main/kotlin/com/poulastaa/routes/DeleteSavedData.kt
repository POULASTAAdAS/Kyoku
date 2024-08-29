package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteSavedData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.DeleteSavedData.route) {
            get {
                val id =
                    call.parameters["id"]?.toLong() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                val type = call.parameters["type"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.deleteSavedData(id, type, payload)

                when (result) {
                    true -> call.respond(HttpStatusCode.OK)
                    false -> call.respond(HttpStatusCode.ServiceUnavailable)
                }
            }
        }
    }
}