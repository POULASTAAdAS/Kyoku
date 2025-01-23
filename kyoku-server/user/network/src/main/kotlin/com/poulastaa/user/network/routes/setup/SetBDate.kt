package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.model.SetBDateReq
import com.poulastaa.user.network.model.SetBDateRes
import com.poulastaa.user.network.model.SetBDateStatus
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.setBDate(
    repo: SetupRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.SetBDate.route) {
            post {
                val req = call.receiveNullable<SetBDateReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val payload = call.getReqUserPayload()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val status = when (repo.setBDate(payload, req.date)) {
                    true -> SetBDateStatus.SUCCESS
                    false -> SetBDateStatus.FAILURE
                }

                call.respond(
                    message = SetBDateRes(status),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}