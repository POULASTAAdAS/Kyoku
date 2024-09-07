package com.poulastaa.routes

import com.poulastaa.data.model.CreatePlaylistPagerFilterTypeDto
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.route_model.req.playlist.CreatePlaylistWithSongReq
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
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

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
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload =
                    call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.updatePlaylist(req, payload)

                when (result) {
                    true -> call.respond(HttpStatusCode.OK)
                    false -> call.respond(HttpStatusCode.ServiceUnavailable)
                }
            }
        }
    }
}

fun Route.createPlaylist(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.CreatePlaylist.route) {
            post {
                val req = call.receiveNullable<CreatePlaylistWithSongReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload =
                    call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.createPlaylist(req, payload)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getCreatePlaylistData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetCreatePlaylistData.route) {
            get {
                val payload = call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getCreatePlaylistData(payload)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getCreatePlaylistPagerData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetCreatePlaylistPagerData.route) {
            get {
                val page = call.parameters["page"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val size = call.parameters["size"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val query = call.parameters["query"]
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val type = call.parameters["type"]
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)


                val filterType = when (type) {
                    CreatePlaylistPagerFilterTypeDto.PLAYLIST.name -> CreatePlaylistPagerFilterTypeDto.PLAYLIST
                    CreatePlaylistPagerFilterTypeDto.ALL.name -> CreatePlaylistPagerFilterTypeDto.ALL
                    CreatePlaylistPagerFilterTypeDto.ARTIST.name -> CreatePlaylistPagerFilterTypeDto.ARTIST
                    CreatePlaylistPagerFilterTypeDto.ALBUM.name -> CreatePlaylistPagerFilterTypeDto.ALBUM
                    else -> return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                }

                val payload = call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getCreatePlaylistPagerData(
                    page = page,
                    size = size,
                    query = query,
                    type = filterType,
                    payload = payload
                )

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}