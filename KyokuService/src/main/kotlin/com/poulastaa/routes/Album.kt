package com.poulastaa.routes

import com.poulastaa.data.model.AlbumPagingType
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.addAlbum(
    service: ServiceRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.AddAlbum.route) {
            get {
                val albumId =
                    call.parameters["albumId"]?.toLong()
                        ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val album = service.addAlbum(albumId, payload)

                call.respond(
                    message = album,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.removeAlbum(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.RemoveAlbum.route) {
            get {
                val albumId =
                    call.parameters["albumId"]?.toLong()
                        ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val status = service.removeAlbum(albumId, payload)

                if (status) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.ServiceUnavailable)
            }
        }
    }
}

fun Route.getAlbumPagingData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetAlbumPaging.route) {
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
                    AlbumPagingType.NAME.name -> AlbumPagingType.NAME
                    AlbumPagingType.BY_YEAR.name -> AlbumPagingType.BY_YEAR
                    AlbumPagingType.BY_POPULARITY.name -> AlbumPagingType.BY_POPULARITY
                    else -> return@get call.respondRedirect(EndPoints.UnAuthorised.route)
                }

                val payload = call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getAlbumPaging(
                    page = page,
                    size = size,
                    query = query,
                    type = reqType,
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