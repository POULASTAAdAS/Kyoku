package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.UserRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.CURRENT_PROJECT_FOLDER
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Route.getCoverImage(
    service: UserRepository,
) {
    route(EndPoints.GetCoverImage.route) {
        get {
            val image =
                call.request.queryParameters["coverImage"]
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

            call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

            val file = File("$CURRENT_PROJECT_FOLDER$COVER_IMAGE_ROOT_DIR/$image")

            call.respondFile(file)
        }
    }
}