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

fun Route.followArtist(
    service: ServiceRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.AddArtist.route) {
            get {
                val artistId =
                    call.parameters["artistId"]?.toLong()
                        ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val artist = service.addArtist(artistId, payload)

                call.respond(
                    message = artist,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.unFollowArtist(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.RemoveArtist.route) {
            get {
                val artistId =
                    call.parameters["artistId"]?.toLong()
                        ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val status = service.removeArtist(artistId, payload)

                if (status) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.ServiceUnavailable)
            }
        }
    }
}