package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.route_model.req.playlist.SavePlaylistReq
import com.poulastaa.domain.model.route_model.req.playlist.UpdatePlaylistReq
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.savePlaylist(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.SavePlaylist.route) {
            post {
                val req = call.receiveNullable<SavePlaylistReq>()
                    ?: return@post call.respondRedirect(EndPoints.SavePlaylist.route)

                val payload = call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.SavePlaylist.route)

                val response = service.savePlaylist(req, payload)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.updatePlaylist(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.UpdatePlaylist.route) {
            post {
                val req = call.receiveNullable<UpdatePlaylistReq>()
                    ?: return@post call.respondRedirect(EndPoints.UpdatePlaylist.route)

                val payload =
                    call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UpdatePlaylist.route)

                val result = service.updatePlaylist(req, payload)

                when (result) {
                    true -> call.respond(HttpStatusCode.OK)
                    false -> call.respond(HttpStatusCode.ServiceUnavailable)
                }
            }
        }
    }
}