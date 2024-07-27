package com.poulastaa.routes

import com.poulastaa.data.model.home.HomeDto
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getLogInData(service: ServiceRepository) {
    route(EndPoints.GetLogInData.route) {
        get {
            val token = call.parameters["token"] ?: return@get call.respond(HomeDto())
            val authKey = call.parameters["authKey"] ?: return@get call.respond(HomeDto())

            val userType = call.parameters["userType"] ?: return@get call.respond(HomeDto())

            val result = service.getLoginData(
                userType = userType,
                token = token,
                authKey = authKey,
            )

            call.respond(
                message = result,
                status = HttpStatusCode.OK,
            )
        }
    }
}