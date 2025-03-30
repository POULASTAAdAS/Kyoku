package com.poulastaa.view.network.routes

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.view.domain.repository.ViewRepository
import com.poulastaa.view.network.mapper.toResponseViewArtist
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getViewArtist(repo: ViewRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.ViewArtist.route) {
            get {
                val artistId = call.parameters["artistId"]?.toLong()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val payload = call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getArtist(artistId, payload)?.toResponseViewArtist()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}