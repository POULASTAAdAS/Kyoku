package com.poulastaa.routes

import com.poulastaa.data.model.artist.ArtistPageReq
import com.poulastaa.data.model.artist.ArtistPageResponse
import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.setup.artist.SuggestArtistResponse
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.artistPage(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.ArtistPage.route) {
            post {
                val req = call.receiveNullable<ArtistPageReq>() ?: return@post call.respond(
                    message = ArtistPageResponse(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = ArtistPageResponse(),
                    status = HttpStatusCode.OK
                )

                val response = service.getArtistPageResponse(req, helper)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}