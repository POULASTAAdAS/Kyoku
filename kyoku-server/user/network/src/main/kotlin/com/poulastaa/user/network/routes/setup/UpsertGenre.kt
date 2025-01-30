package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.core.network.mapper.toResponseGenre
import com.poulastaa.core.network.model.UpsertOperation
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.mapper.toDtoUpsertGenre
import com.poulastaa.user.network.model.SaveGenreRes
import com.poulastaa.user.network.model.UpsertGenreReq
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.upsertGenre(repo: SetupRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.UPSERTGenre.route) {
            post {
                val req = call.receiveNullable<UpsertGenreReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val payload = call.getReqUserPayload()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.upsertGenre(
                    userPayload = payload,
                    req = req.data.toDtoUpsertGenre()
                )

                when (req.data.operation) {
                    UpsertOperation.INSERT -> call.respond(
                        message = SaveGenreRes(
                            list = result.map { it.toResponseGenre() }
                        ),
                        status = HttpStatusCode.OK
                    )

                    UpsertOperation.UPDATE -> TODO()
                    UpsertOperation.DELETE -> TODO()
                }
            }
        }
    }
}