package com.poulastaa.search.route

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

fun Route.artistAlbumPaging(
    repo: PagingRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Paging.GetArtistPagingAlbums.route) {
            get {
                val param = call.getPagingParams() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                val artistId = call.parameters["artistId"]?.toLongOrNull()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getArtistPagingAlbum(
                    page = param.page,
                    size = param.size,
                    query = param.query,
                    artistId = artistId
                ).map {
                    it.toResponseExploreItem()
                }

                call.respond(
                    status = HttpStatusCode.OK,
                    message = result
                )
            }
        }
    }
}