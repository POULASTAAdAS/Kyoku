package com.poulastaa.routes.sertup

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.setup.artist.SuggestArtistReq
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

fun Route.suggestArtist(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        post(EndPoints.SuggestArtist.route) {
        val req = call.receiveNullable<SuggestArtistReq>() ?: return@post call.respond(
            message = SuggestArtistResponse(),
            status = HttpStatusCode.OK
        )

        val helper = getUserType() ?: return@post call.respond(
            message = SuggestArtistResponse(),
            status = HttpStatusCode.OK
        )

        val response = service.suggestArtist(req, helper)

        call.respond(
            message = response,
            status = HttpStatusCode.OK
        )
    }
    }
}