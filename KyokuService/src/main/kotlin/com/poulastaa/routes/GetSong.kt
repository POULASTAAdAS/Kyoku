package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.addToFavourite(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.AddToFavourite.route) {
            get {
                val songId = call.request.queryParameters["songId"]?.toLong() ?: return@get call.respondRedirect(
                    EndPoints.UnAuthorised.route
                )

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val song = service.addToFavourite(songId, payload)

                call.respond(song)
            }
        }
    }
}