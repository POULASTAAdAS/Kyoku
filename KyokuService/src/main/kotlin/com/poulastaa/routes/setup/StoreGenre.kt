package com.poulastaa.routes.setup

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.route_model.req.setup.StoreGenreReq
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.storeGenre(
    service: ServiceRepository,
) {
    route(EndPoints.StoreGenre.route) {
        put {
            val request = call.receiveNullable<StoreGenreReq>()
                ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)
            val payload = call.getReqUserPayload() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)

            val response = service.storeGenre(
                userPayload = payload,
                genreIds = request.idList
            )

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}