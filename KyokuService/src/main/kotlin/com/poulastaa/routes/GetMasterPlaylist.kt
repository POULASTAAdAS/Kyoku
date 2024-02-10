package com.poulastaa.routes

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getMasterPlaylist(
    service: UserServiceRepository
) {
    authenticate("jwt-auth", "google-auth", "passkey-auth") {
        route(EndPoints.PlaySongMaster.route) {
            get {
                val playlist =
                    call.parameters["playlist"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)


            }
        }
    }
}