package com.poulastaa.routes

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.data.model.home.ResponsePlaylist
import com.poulastaa.data.model.playlist.AddSongToPlaylistReq
import com.poulastaa.data.model.playlist.CreatePlaylistReq
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.getPlaylistOnSongId(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.PlaylistOnSongId.route) {
            post {
                val req = call.receiveNullable<CreatePlaylistReq>() ?: return@post call.respond(
                    message = ResponsePlaylist(),
                    status = HttpStatusCode.OK
                )

                if (req.listOfSongId.isEmpty() || req.name.isEmpty()) return@post call.respond(
                    message = ResponsePlaylist(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = ResponsePlaylist(),
                    status = HttpStatusCode.OK
                )

                val response = service.createPlaylist(helper, req)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getPlaylistOnfAlbumId(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.PlaylistOnAlbumId.route) {
            post {
                val req = call.receiveNullable<CreatePlaylistReq>() ?: return@post call.respond(
                    message = ResponsePlaylist(),
                    status = HttpStatusCode.OK
                )

                if (req.albumId == -1L || req.name.isEmpty()) call.respond(
                    message = ResponsePlaylist(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = ResponsePlaylist(),
                    status = HttpStatusCode.OK
                )

                val response = service.createPlaylist(helper, req)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.addSongToPlaylist(
    service: UserServiceRepository
) {
    authenticate(configurations = SECURITY_LIST) {
        route(EndPoints.AddSongToPlaylist.route) {
            post {
                val req = call.receiveNullable<AddSongToPlaylistReq>() ?: return@post call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                if (req.songId == -1L) call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                val response = service.editSongAndPlaylist(helper, req)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}