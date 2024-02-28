package com.poulastaa.routes.sertup

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.setup.artist.StoreArtistReq
import com.poulastaa.data.model.setup.artist.StoreArtistResponse
import com.poulastaa.data.model.setup.artist.SuggestArtistResponse
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.storeArtist(service: UserServiceRepository) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.StoreArtist.route) {
            post {
                val req = call.receiveNullable<StoreArtistReq>() ?: return@post call.respond(
                    message = StoreArtistResponse(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = SuggestArtistResponse(),
                    status = HttpStatusCode.OK
                )

                val response = service.storeArtist(req, helper)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}