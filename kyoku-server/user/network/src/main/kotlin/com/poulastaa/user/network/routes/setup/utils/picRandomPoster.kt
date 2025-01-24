package com.poulastaa.user.network.routes.setup.utils

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.CURRENT_PROJECT_FOLDER
import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM
import com.poulastaa.core.network.getReqUserPayload
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

internal fun Route.picRandomPoster() {
    get {
        val url = call.request.queryParameters[POSTER_PARAM]
            ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

        call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

        val path = "$CURRENT_PROJECT_FOLDER$url"
        val folder = File(path)

        if (!folder.exists() || !folder.isDirectory) return@get call.respond(HttpStatusCode.NotFound)

        val file = folder.listFiles()?.random()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        call.respondFile(file)
    }
}