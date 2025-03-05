package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.model.GetBDateRes
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getBDate(repo: SetupRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.GetBDate.route) {
            get {
                val payload = call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                when (val data = repo.getBDate(payload)) {
                    null -> call.respondRedirect(EndPoints.UnAuthorized.route)
                    else -> call.respond(
                        message = GetBDateRes(data),
                        status = HttpStatusCode.OK
                    )
                }
            }
        }
    }
}