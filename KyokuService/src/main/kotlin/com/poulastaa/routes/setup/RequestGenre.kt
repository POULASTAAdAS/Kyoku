package com.poulastaa.routes.setup

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.route_model.req.setup.SuggestGenreReq
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.requestGenre(
    service: ServiceRepository,
) {
    route(EndPoints.SuggestGenre.route) {
        post {
            val request = call.receiveNullable<SuggestGenreReq>()
                ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)
            val payload = call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

            val response = service.getGenre(
                userPayload = payload,
                genreIds = request.listOfSentGenreId
            )

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}