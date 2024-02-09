package com.poulastaa.routes

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.getClaimFromPayload
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// todo delete from here

fun Route.getUserProfilePic(
    userService: UserServiceRepository
) {
    authenticate("jwt-auth") {
        route(EndPoints.ProfilePic.route) {
            get {
                val email = call.getClaimFromPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val file = userService.getUserProfilePic(email = email)

                if (file != null && file.exists()) {
                    call.respondFile(file)
                    return@get
                }

                call.respond(
                    message = "File Not Found",
                    status = HttpStatusCode.NotFound
                )
            }
        }
    }
}