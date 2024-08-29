package com.poulastaa.routes

import com.poulastaa.data.model.GetDataReq
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getListOfData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetTypeData.route) {
            post {
                val req =
                    call.receiveNullable<GetDataReq>() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload() ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getListOfData(req, payload)

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK,
                )
            }
        }
    }
}