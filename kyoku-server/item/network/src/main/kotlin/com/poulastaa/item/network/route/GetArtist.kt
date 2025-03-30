package com.poulastaa.item.network.route

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.core.network.mapper.toResponseArtist
import com.poulastaa.item.domain.repository.ItemRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getArtist(repo: ItemRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Item.GetArtist.route) {
            get {
                val artistId = call.parameters["artistId"]?.toLongOrNull()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getArtist(artistId)
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                call.respond(status = HttpStatusCode.OK, message = result.toResponseArtist())
            }
        }
    }
}