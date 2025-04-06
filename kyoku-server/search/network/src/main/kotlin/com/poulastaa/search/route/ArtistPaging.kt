package com.poulastaa.search.route

import com.poulastaa.core.domain.model.DtoExploreArtistFilterType
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.search.mapper.toResponseExploreItem
import com.poulastaa.search.repository.PagingRepository
import com.poulastaa.search.utils.getPagingParams
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.artistPaging(repo: PagingRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Paging.GetPagingArtist.route) {
            get {
                val prams = call.getPagingParams() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                val filterType = try {
                    DtoExploreArtistFilterType.valueOf(
                        call.parameters["filterType"] ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                    )
                } catch (_: Exception) {
                    return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                }

                val result = repo.getPagingArtist(
                    query = prams.query,
                    page = prams.page,
                    filterType = filterType,
                    size = prams.size,
                )

                call.respond(
                    status = HttpStatusCode.OK,
                    message = result.map { it.toResponseExploreItem() }
                )
            }
        }
    }
}