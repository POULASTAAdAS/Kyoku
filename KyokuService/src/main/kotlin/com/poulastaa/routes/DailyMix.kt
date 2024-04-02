package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.home.DailyMixPreview
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.dailyMix(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.DailyMix.route) {
            get {
                val helper = getUserType() ?: return@get call.respond(
                    message = DailyMixPreview(),
                    status = HttpStatusCode.OK
                )

                val response = service.getDailyMix(helper)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}