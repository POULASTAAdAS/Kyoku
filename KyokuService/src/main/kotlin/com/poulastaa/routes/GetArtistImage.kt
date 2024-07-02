package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.ARTIST_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.CURRENT_PROJECT_FOLDER
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.getArtistImage() {
    route(EndPoints.GetArtistImage.route) {
        get {
            val image =
                call.request.queryParameters["artistCover"]?.replace("_", " ")
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

            call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

            val folderPath = "$CURRENT_PROJECT_FOLDER$ARTIST_IMAGE_ROOT_DIR/$image"


            val file = pictRandom(folderPath) ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

            call.respondFile(file)
        }
    }
}

private fun pictRandom(path: String): File? {
    val folder = File(path)

    if (!folder.exists() || !folder.isDirectory) return null

    return folder.listFiles()?.random()
}