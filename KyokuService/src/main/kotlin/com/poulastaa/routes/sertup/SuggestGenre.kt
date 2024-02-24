package com.poulastaa.routes.sertup

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.data.model.setup.suggest_genre.SuggestGenreResponse
import com.poulastaa.data.model.setup.suggest_genre.SuggestGenreResponseStatus
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.suggestGenre(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.SuggestGenre.route) {
            post {
                val req = call.receiveNullable<SuggestGenreReq>() ?: return@post call.respond(
                    message = emptyList<SuggestGenreReq>(),
                    status = HttpStatusCode.OK
                )

                val user = getUserType() ?: return@post call.respond(
                    message = SuggestGenreResponse(
                        status = SuggestGenreResponseStatus.FAILURE
                    ),
                    status = HttpStatusCode.OK
                )

                val response = service.suggestGenre(req, user)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}