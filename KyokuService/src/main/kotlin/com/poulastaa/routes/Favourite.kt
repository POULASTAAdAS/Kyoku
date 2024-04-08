package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.insertIntoFavourite(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.AddSongToFavourite.route) {
            get {
                val req = call.parameters["songId"] ?: return@get call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@get call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                val response = service.insertIntoFavourite(helper, req.toLong())

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}


fun Route.removeFromFavourite(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.RemoveSongFromFavourite.route) {
            get {
                val req = call.parameters["songId"] ?: return@get call.respond(
                    message = false,
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@get call.respond(
                    message = false,
                    status = HttpStatusCode.OK
                )

                val response = service.removeFromFavourite(helper, req.toLong())

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}