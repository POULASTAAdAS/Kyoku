package com.poulastaa.routes

import com.poulastaa.data.model.EndPoints
import com.poulastaa.utils.Constants
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.getSongCover() {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.CoverImage.route) {
            get {
                val coverImage = call.parameters["coverImage"] ?: return@get

                val file = try {
                    File("${Constants.COVER_IMAGE_ROOT_DIR}$coverImage")
                } catch (e: Exception) {
                    null
                } ?: return@get

                call.respondFile(file)
            }
        }
    }
}