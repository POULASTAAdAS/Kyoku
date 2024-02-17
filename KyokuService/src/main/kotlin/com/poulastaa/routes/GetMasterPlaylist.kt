package com.poulastaa.routes

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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