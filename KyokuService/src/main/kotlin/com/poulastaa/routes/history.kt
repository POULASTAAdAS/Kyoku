package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.removeFromHistory(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.RemoveFromHistory.route) {
            get {
                val songId = call.parameters["songId"] ?: return@get call.respond(
                    message = false,
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@get call.respond(
                    message = false,
                    status = HttpStatusCode.OK
                )

                val response = service.removeFromHistory(songId.toLong(), helper)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}