package com.poulastaa.routes

import com.poulastaa.data.model.*
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.route_ext.getReqUserPayload
import com.poulastaa.utils.Constants.SECURITY_LIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.syncData(service: ServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.SyncData.route) {
            post {
                val req = call.receiveNullable<UpdateSavedDataReq>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val payload = call.getReqUserPayload()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

                val result = service.getSyncData(
                    req = req,
                    payload = payload
                )

                when (req.type) {
                    UpdateSavedDataType.ALBUM -> {
                        val res = result as SyncDto<AlbumWithSongDto>

                        call.respond(
                            message = res,
                            status = HttpStatusCode.OK
                        )
                    }

                    UpdateSavedDataType.PLAYLIST,
                    UpdateSavedDataType.PLAYLIST_SONG,
                        -> {
                        val res = result as SyncDto<PlaylistDto>

                        call.respond(
                            message = res,
                            status = HttpStatusCode.OK
                        )
                    }

                    UpdateSavedDataType.ARTIST -> {
                        val res = result as SyncDto<ArtistDto>

                        call.respond(
                            message = res,
                            status = HttpStatusCode.OK
                        )
                    }

                    UpdateSavedDataType.FEV -> {
                        val res = result as SyncDto<SongDto>

                        call.respond(
                            message = res,
                            status = HttpStatusCode.OK
                        )
                    }
                }
            }
        }
    }
}