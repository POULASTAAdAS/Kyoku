package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.home.HomeReq
import com.poulastaa.data.model.home.HomeResponse
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.home(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Home.route) {
            post {
                val req = call.receiveNullable<HomeReq>() ?: return@post call.respond(
                    message = HomeResponse(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = HomeResponse(),
                    status = HttpStatusCode.OK
                )

                val response = service.generateHomeResponse(req, helper)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}