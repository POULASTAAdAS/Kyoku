package com.poulastaa.search.route

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.search.mapper.toResponseArtistPagingItem
import com.poulastaa.search.repository.ArtistPagingRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.artistAlbumPaging(
    repo: ArtistPagingRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Paging.GetArtistPagingAlbums.route) {
            get {
                val page = call.parameters["page"]?.toIntOrNull()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                val size = call.parameters["size"]?.toIntOrNull()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                val artistId = call.parameters["artistId"]?.toLongOrNull()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val query = call.request.queryParameters["query"]

                call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getPagingAlbum(page, size, query, artistId).map {
                    it.toResponseArtistPagingItem()
                }

                call.respond(
                    status = HttpStatusCode.OK,
                    message = result
                )
            }
        }
    }
}