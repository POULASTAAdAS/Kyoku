package com.poulastaa.item.network.route

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.core.network.mapper.toResponsePlaylist
import com.poulastaa.item.domain.repository.ItemRepository
import com.poulastaa.item.network.model.CreatePlaylistRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPlaylist(repo: ItemRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Item.CreatePlaylist.route) {
            post {
                val request = call.receiveNullable<CreatePlaylistRequest>()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val payload = call.getReqUserPayload()
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.createPlaylist(request.playlistName, payload)
                    ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

                call.respond(
                    status = HttpStatusCode.OK,
                    message = result.toResponsePlaylist()
                )
            }
        }
    }
}