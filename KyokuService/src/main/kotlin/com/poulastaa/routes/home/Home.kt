package com.poulastaa.routes.home

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.home.HomeReq
import com.poulastaa.data.model.home.HomeResponse
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.home() {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Home.route) {
            post {
                val req = call.receiveNullable<HomeReq>() ?: call.respond(
                    message = HomeResponse(),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}