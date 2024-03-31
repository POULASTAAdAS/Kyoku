package com.poulastaa.routes

import com.poulastaa.data.model.artist.ArtistAlbum
import com.poulastaa.data.model.artist.ArtistPageReq
import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.home.SongPreview
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.artistSongPage(
    service: UserServiceRepository
){
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.ArtistPageSongs.route) {
            post {
                val req = call.receiveNullable<ArtistPageReq>() ?: return@post call.respond(
                    message = emptyList<SongPreview>(),
                    status = HttpStatusCode.OK
                )

                getUserType() ?: return@post call.respond(
                    message = emptyList<SongPreview>(),
                    status = HttpStatusCode.OK
                )

                val response = service.getArtistPageSongResponse(req)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}