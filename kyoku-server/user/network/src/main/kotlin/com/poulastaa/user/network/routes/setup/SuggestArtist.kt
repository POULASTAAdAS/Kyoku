package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.mapper.toSuggestedArtistRes
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.suggestArtist(repo: SetupRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.SuggestArtist.route) {
            get {
                val page = call.parameters["page"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                val query = call.parameters["query"]
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                val size = call.parameters["size"]?.toInt()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                if (size <= 0) return@get call.respondRedirect(EndPoints.UnAuthorized.route)
                if (size >= 50) return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                call.getReqUserPayload()
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getArtist(page, size, query).toSuggestedArtistRes()

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}