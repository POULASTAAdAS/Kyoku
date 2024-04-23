package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getSongOnId(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.GetSongOnId.route) {
            get {
                val songId = call.parameters["songId"] ?: return@get call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                getUserType() ?: return@get call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                val response = service.getSongOnId(songId.toLong())

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getMasterPlaylist(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.PlaySongMaster.route) {
            get {
                val playlist =
                    call.parameters["playlist"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)


            }
        }
    }
}