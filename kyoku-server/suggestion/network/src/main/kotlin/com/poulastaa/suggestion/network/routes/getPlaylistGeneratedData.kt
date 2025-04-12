package com.poulastaa.suggestion.network.routes

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.suggestion.domain.repository.SuggestionRepository
import com.poulastaa.suggestion.network.mapper.toAddSongToPlaylistPageItemResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getPlaylistGeneratedData(repo: SuggestionRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.CreatePlaylistStaticData.route) {
            get {
                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getAddSongToPlaylistData(payload)
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                call.respond(
                    status = HttpStatusCode.OK,
                    message = result.map { it.toAddSongToPlaylistPageItemResponse() }
                )
            }
        }
    }
}