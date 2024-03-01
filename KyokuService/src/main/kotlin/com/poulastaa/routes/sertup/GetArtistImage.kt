package com.poulastaa.routes.sertup

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.utils.Constants.ARTIST_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.getArtistImage() {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetArtistImageUrl.route) {
            get {
                val artistName = call.parameters["name"]?.replace("_", " ") ?: return@get

                val image = pictRandom("$ARTIST_IMAGE_ROOT_DIR$artistName") ?: return@get

                call.respondFile(file = image)
            }
        }
    }
}

private fun pictRandom(path: String): File? {
    val folder = File(path)

    if (!folder.exists() || !folder.isDirectory) return null

    return folder.listFiles()?.random()
}
