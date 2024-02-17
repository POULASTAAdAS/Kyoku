package com.poulastaa.routes.sertup

import com.poulastaa.data.model.EndPoints
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.server.routing.*

fun Route.selectGenre(
    service: UserServiceRepository
) {
//    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.SelectGenre.route) {
            post {
//                val nameToBeDefined = call.parameters["nameToBeDefined"] ?: return@post call.respond(
//                    message = "",
//                    status = HttpStatusCode.OK
//                )

                // get country and speaking language
            }
//        }
    }
}