package com.poulastaa.view.network.routes

import com.poulastaa.core.domain.model.*
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

                val savedSongIdList =
                    call.parameters["savedSongIdList"]?.split(",")?.map { it.toLongOrNull() }?.mapNotNull { it }

                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                when (type) {
                    ViewTypeReq.ALBUM -> (repo.getViewTypeData(
                        type = DtoViewType.ALBUM,
                        otherId = otherId,
                        payload = payload,
                        songIds = savedSongIdList
                    ) as DtoViewOtherPayload<DtoDetailedPrevSong>?)?.toViewResponse()?.let {
                        call.respond(
                            message = it,
                            status = HttpStatusCode.OK
                        )
                    }

                    ViewTypeReq.PLAYLIST,
                    ViewTypeReq.FAVOURITE,
                    ViewTypeReq.POPULAR_SONG_MIX,
                    ViewTypeReq.POPULAR_YEAR_MIX,
                    ViewTypeReq.SAVED_ARTIST_SONG_MIX,
                    ViewTypeReq.DAY_TYPE_MIX,
                        -> (repo.getViewTypeData(
                        type = type.toDtoViewType(),
                        otherId = otherId,
                        payload = payload,
                        songIds = savedSongIdList
                    ) as DtoViewOtherPayload<DtoSong>?)?.toViewResponse()?.let {
                        call.respond(
                            message = it,
                            status = HttpStatusCode.OK
                        )
                    }
                } ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
            }
        }
    }
}