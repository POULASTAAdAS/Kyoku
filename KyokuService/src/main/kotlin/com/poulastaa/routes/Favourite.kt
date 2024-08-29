package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.route_model.req.favourite.UpdateFavouriteReq
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
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

                call.respond(
                    message = song,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.removeFromFavourite(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.RemoveFromFavourite.route) {
            get {
                val songId = call.request.queryParameters["songId"]?.toLong() ?: return@get call.respondRedirect(
                    EndPoints.UnAuthorised.route
                )

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val status = service.removeFromFavourite(songId, payload)

                if (status) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.ServiceUnavailable)
            }
        }
    }
}

fun Route.updateFavourite(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.UpdateFavourite.route) {
            post {
                val req = call.receiveNullable<UpdateFavouriteReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                when (req.opp) {
                    true -> {
                        service.addToFavourite(req.songId, payload)

                        call.respond(HttpStatusCode.OK)
                    }

                    false -> when (service.removeFromFavourite(req.songId, payload)) {
                        true -> call.respond(HttpStatusCode.OK)
                        false -> call.respond(HttpStatusCode.ServiceUnavailable)
                    }
                }
            }
        }
    }
}

