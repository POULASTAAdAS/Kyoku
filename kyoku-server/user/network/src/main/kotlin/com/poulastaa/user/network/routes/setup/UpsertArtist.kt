package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.core.network.mapper.toDtoUpsertOperation
import com.poulastaa.core.network.mapper.toResponseArtist
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.mapper.toDtoUpsertArtist
import com.poulastaa.user.network.model.SaveArtistRes
import com.poulastaa.user.network.model.UpsertArtistReq
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.upsertArtist(repo: SetupRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.UPSERTArtist.route) {
            post {
                val req = call.receiveNullable<UpsertArtistReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val payload = call.getReqUserPayload()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.upsertArtist(
                    userPayload = payload,
                    req = req.data.toDtoUpsertArtist()
                )

                call.respond(
                    message = SaveArtistRes(result.map { it.toResponseArtist() }),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}