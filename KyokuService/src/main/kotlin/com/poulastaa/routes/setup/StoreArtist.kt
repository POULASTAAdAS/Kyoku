package com.poulastaa.routes.setup

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.route_model.req.setup.StoreArtistReq
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.storeArtist(
    service: ServiceRepository,
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.StoreArtist.route) {
            put {
                val request = call.receiveNullable<StoreArtistReq>()
                    ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)
                val payload = call.getReqUserPayload() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)

                val response = service.storeArtist(
                    userPayload = payload,
                    artistIds = request.idList
                )

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}