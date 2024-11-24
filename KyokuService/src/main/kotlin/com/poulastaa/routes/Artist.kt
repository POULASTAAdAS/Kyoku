package com.poulastaa.routes

import com.poulastaa.data.model.AddArtistReq
import com.poulastaa.data.model.ArtistPagingTypeDto
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followArtist(
    service: ServiceRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.AddArtist.route) {
            post {
                val req =
                    call.receiveNullable<AddArtistReq>()
                        ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val artist = service.addArtist(req.list, payload)

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

fun Route.viewArtist(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.ViewArtist.route) {
            get {
                val req = call.parameters["artistId"]?.toLong()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getViewArtistData(req, payload)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getArtist(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetArtist.route) {
            get {
                val req = call.parameters["artistId"]?.toLong()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getArtistOnId(req, payload)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}


fun Route.getArtistSongPagerData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetArtistSong.route) {
            get {
                val artistId = call.parameters["artistId"]?.toLong()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                val page = call.parameters["page"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                val size = call.parameters["size"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val sendSongIdListStr = call.parameters["removeSongIdList"]

                val sendSongIdList = sendSongIdListStr?.let {
                    if (it.isNotBlank()) it.split(",").map { id -> id.toLong() } else null
                } ?: emptyList()

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getArtistSongPagingData(artistId, page, size, payload, sendSongIdList)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getArtistAlbumPagerData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetArtistAlbum.route) {
            get {
                val artistId = call.parameters["artistId"]?.toLong()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                val page = call.parameters["page"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                val size = call.parameters["size"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getArtistAlbumPagingData(artistId, page, size, payload)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getArtistPagingData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetArtistPaging.route) {
            get {
                val page = call.parameters["page"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val size = call.parameters["size"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val query = call.parameters["query"]
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val type = call.parameters["type"]
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val reqType = when (type) {
                    ArtistPagingTypeDto.ALL.name -> ArtistPagingTypeDto.ALL
                    ArtistPagingTypeDto.INTERNATIONAL.name -> ArtistPagingTypeDto.INTERNATIONAL
                    else -> return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                }

                val payload = call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getArtistPaging(
                    page = page,
                    size = size,
                    query = query,
                    type = reqType,
                    payload = payload,
                )

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getSongArtist(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetSongArtist.route) {
            get {
                val songId = call.parameters["songId"]?.toLong()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getSongArtist(songId, payload)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}