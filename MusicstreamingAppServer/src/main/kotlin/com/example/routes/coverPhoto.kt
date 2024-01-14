package com.example.routes

import com.example.data.model.EndPoints
import com.example.domain.repository.song_db.SongRepository
import com.example.util.Constants.COVER_IMAGE_ROOT_DIR
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getCoverPhoto(
    songRepository: SongRepository
) {
    authenticate("jwt-auth", "google-auth") {
        route(EndPoints.CoverImage.route) {
            get {
                val coverImage = call.parameters["coverImage"] ?: return@get

                songRepository.getCoverImage(
                    path = "$COVER_IMAGE_ROOT_DIR$coverImage"
                )?.let {
                    call.respondFile(file = it)

                    return@get
                }

                call.respond(
                    message = "no cover photo found",
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}