package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.pinned.PinnedReq
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.handlePinned(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.Pinned.route) {
            post {
                val req = call.receiveNullable<PinnedReq>() ?: return@post call.respond(
                    message = false,
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = false,
                    status = HttpStatusCode.OK
                )

                val response = service.handlePinnedOperation(helper, req)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}