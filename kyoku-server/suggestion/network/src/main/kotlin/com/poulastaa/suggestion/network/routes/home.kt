package com.poulastaa.suggestion.network.routes

import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.suggestion.domain.repository.SuggestionRepository
import com.poulastaa.suggestion.network.mapper.toResponseHome
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.home(repo: SuggestionRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.Home.route) {
            get {
                val payload = call.getReqUserPayload() ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                val result = repo.getHomeData(payload) ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

                call.respond(
                    message = result.toResponseHome(),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}