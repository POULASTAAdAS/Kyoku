package com.poulastaa.search.route

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.search.mapper.toAddSongToPlaylistItem
import com.poulastaa.search.mapper.toDtoAddSongToPlaylistSearchFilterType
import com.poulastaa.search.model.AddSongToPlaylistSearchFilterTypeRequest
import com.poulastaa.search.repository.PagingRepository
import com.poulastaa.search.utils.getPagingParams
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPlaylistPaging(
    repo: PagingRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Paging.GetCreatePlaylist.route) {
            get {
                val pagingPayload = call.getPagingParams()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                val filterType = try {
                    AddSongToPlaylistSearchFilterTypeRequest.valueOf(
                        value = call.parameters["filterType"] ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                    )
                } catch (_: Exception) {
                    return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                }

                val result = repo.getPagingCreatePlaylist(
                    page = pagingPayload.page,
                    size = pagingPayload.size,
                    query = pagingPayload.query,
                    filterType = filterType.toDtoAddSongToPlaylistSearchFilterType()
                )

                call.respond(
                    status = HttpStatusCode.OK,
                    message = result.map { it.toAddSongToPlaylistItem() }
                )
            }
        }
    }
}