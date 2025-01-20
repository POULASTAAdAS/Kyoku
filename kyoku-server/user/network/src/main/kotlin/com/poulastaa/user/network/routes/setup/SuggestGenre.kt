package com.poulastaa.user.network.routes.setup

import com.poulastaa.core.domain.model.Endpoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.getReqUserPayload
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.network.mapper.toSuggestGenreDto
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.suggestGenre(
    repo: SetupRepository,
) {
    authenticate(configurations = SECURITY_LIST) {
        route(Endpoints.PicGenre.route) {
            get {
                val sentGenreIds = call.parameters["genreIds"]?.split(",")?.map { it.toInt() }
                    ?: return@get call.respondRedirect(Endpoints.UnAuthorized.route)

                call.getReqUserPayload()
                    ?: return@get call.respondRedirect(Endpoints.UnAuthorized.route)

                val result = repo.getGenre(
                    genreIds = sentGenreIds
                ).toSuggestGenreDto()

                call.respond(
                    message = result,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}