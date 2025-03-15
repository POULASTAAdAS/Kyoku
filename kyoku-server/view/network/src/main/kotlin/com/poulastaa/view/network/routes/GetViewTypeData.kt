package com.poulastaa.view.network.routes

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.view.domain.repository.ViewRepository
import com.poulastaa.view.network.mapper.toDtoViewType
import com.poulastaa.view.network.mapper.toViewResponse
import com.poulastaa.view.network.model.ViewTypeReq
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getViewTypeData(repo: ViewRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.ViewOther.route) {
            get {
                val type = try {
                    ViewTypeReq.valueOf(
                        call.parameters["type"]
                            ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                    )
                } catch (_: Exception) {
                    return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                }

                val otherId = call.parameters["otherId"]?.toLongOrNull()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val savedSongIdList =
                    call.parameters["savedSongIdList"]?.split(",")?.map { it.toLongOrNull() }?.mapNotNull { it }

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getViewTypeData(
                    type = type.toDtoViewType(),
                    otherId = otherId,
                    payload = payload,
                    songIds = savedSongIdList
                ) ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                call.respond(
                    message = result.toViewResponse(),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}