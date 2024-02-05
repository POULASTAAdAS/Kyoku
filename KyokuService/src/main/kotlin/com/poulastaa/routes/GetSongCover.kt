package com.poulastaa.routes

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.song.SongRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import io.ktor.server.auth.*

fun Route.getSongCover(
    songRepository: SongRepository
) {
    authenticate("jwt-auth" , "google-auth" , "passkey-auth") {
        route(EndPoints.CoverImage.route) {
            get {
                val coverImage = call.parameters["coverImage"] ?: return@get

                val file = songRepository.getCoverImage(
                    path = "$COVER_IMAGE_ROOT_DIR$coverImage"
                )

                if (file != null) {
                    call.respondFile(file)

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