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

fun Route.getSong(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetSong.route) {
            get {
                val songId = call.parameters["songId"]?.toLongOrNull()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val song = service.getSong(songId)

                call.respond(
                    message = song,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}