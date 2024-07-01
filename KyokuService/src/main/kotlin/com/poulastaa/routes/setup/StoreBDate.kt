package com.poulastaa.routes.setup

import com.poulastaa.data.model.req.setup.StoreBDateReq
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.storeBDate(
    service: ServiceRepository,
) {
    route(EndPoints.StoreBDate.route) {
        put {
            val req =
                call.receiveNullable<StoreBDateReq>() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)

            val payload = call.getReqUserPayload() ?: return@put call.respondRedirect(EndPoints.UnAuthorised.route)

            val response = service.updateBDate(
                userPayload = payload,
                date = req.bDate,
            )

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}