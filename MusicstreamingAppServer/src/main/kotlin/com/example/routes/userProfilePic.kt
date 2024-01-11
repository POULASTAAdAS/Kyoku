package com.example.routes

import com.example.data.model.EndPoints
import com.example.domain.repository.UserServiceRepository
import com.example.util.getClaimFromPayload
import com.example.util.respondFile
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserProfilePic(
    userService: UserServiceRepository,
) {
    authenticate("jwt-auth") {
        route(EndPoints.ProfilePic.route) {
            get {
                val claim = call.getClaimFromPayload()
//                val sub = call.authentication.getSub()

                claim?.let {
                    userService.getUserProfilePic(email = it)?.let { file ->
                        respondFile(file)
                        return@get
                    }


                    call.respond(
                        message = "",
                        status = HttpStatusCode.NotFound
                    )

                    return@get
                }

//                sub?.let {
//
//
//                    return@get
//                }

                call.respondRedirect(EndPoints.UnAuthorised.route)
            }
        }
    }
}

