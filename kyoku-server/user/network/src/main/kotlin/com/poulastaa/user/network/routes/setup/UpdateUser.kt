package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.model.UpdateUsernameReq
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.updateUser(repo: SetupRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.UpdateUsername.route) {
            put {
                val req = call.receiveNullable<UpdateUsernameReq>()
                    ?: return@put call.respondRedirect(EndPoints.UnAuthorized.route)

                val payload = call.getReqUserPayload()
                    ?: return@put call.respondRedirect(EndPoints.UnAuthorized.route)

                when (repo.updateUsername(payload, req.username)) {
                    true -> call.respond(HttpStatusCode.OK)
                    false -> call.respond(HttpStatusCode.BadRequest)
                    null -> call.respondRedirect(EndPoints.UnAuthorized.route)
                }
            }
        }
    }
}