package com.poulastaa.routes

import com.poulastaa.data.model.artist.ArtistAlbum
import com.poulastaa.data.model.artist.ArtistPageReq
import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.artistAlbumPage(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.ArtistPageAlbum.route) {
            post {
                val req = call.receiveNullable<ArtistPageReq>() ?: return@post call.respond(
                    message = emptyList<ArtistAlbum>(),
                    status = HttpStatusCode.OK
                )

                getUserType() ?: return@post call.respond(
                    message = emptyList<ArtistAlbum>(),
                    status = HttpStatusCode.OK
                )

                val response = service.artistPageAlbumResponse(req)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}