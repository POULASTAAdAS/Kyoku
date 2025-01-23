package com.poulastaa.user.network.routes

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.CURRENT_PROJECT_FOLDER
import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.getSongPoster() {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Poster.SongPoster.route) {
            get {
                val url = call.parameters[POSTER_PARAM] ?: return@get call.respond(HttpStatusCode.BadRequest)

                try {
                    val file = File("$CURRENT_PROJECT_FOLDER$url")
                    call.respondFile(file)
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}