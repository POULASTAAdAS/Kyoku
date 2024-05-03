package com.poulastaa.routes

import com.poulastaa.data.model.artist.SongListReq
import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.Constants.BASE_URL
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR
import com.poulastaa.utils.Constants.PLAYLIST
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.io.File

fun Route.getSongOnId(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.GetSongOnId.route) {
            get {
                val songId = call.parameters["songId"] ?: return@get call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@get call.respond(
                    message = ResponseSong(),
                    status = HttpStatusCode.OK
                )

                val response = service.getSongOnId(songId.toLong(), helper)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}

fun Route.getSongOnListonIdList(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.GetSongOnListonIdList.route) {
            post {
                val req = call.receiveNullable<SongListReq>() ?: return@post call.respond(
                    message = emptyList<ResponseSong>(),
                    status = HttpStatusCode.OK
                )

                val helper = getUserType() ?: return@post call.respond(
                    message = emptyList<ResponseSong>(),
                    status = HttpStatusCode.OK
                )

                val response = service.getListOfSong(req.listOfSongId, helper)

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}


fun Route.getMasterPlaylist(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.PlaySongMaster.route) {
            get {
                val playlist =
                    call.parameters["master"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val file = File(MASTER_PLAYLIST_ROOT_DIR + playlist)

                val masterPlaylist = StringBuilder()

                file.forEachLine {
                    println(it)
                }

                file.forEachLine {
                    if (it.endsWith(".m3u8")) {
                        masterPlaylist.appendLine(
                            "${BASE_URL + EndPoints.PlaySongPlaylist.route}?playlist=${
                                it.removePrefix("F:/songs/")
                            }"
                        )
                    } else masterPlaylist.appendLine(it)
                }

                call.respondText(masterPlaylist.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
            }
        }
    }
}

fun Route.get_128_Or_320_Playlist(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.PlaySongPlaylist.route) {
            get {
                val playlist =
                    call.parameters["playlist"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val pair =
                    if (playlist.startsWith("128")) "128/" + playlist.split('/')[1] to File(PLAYLIST + playlist)
                    else "320/" + playlist.split('/')[1] to File(PLAYLIST + playlist)

                val masterPlaylist = StringBuilder()

                pair.second.forEachLine {
                    println(it)
                }

                pair.second.forEachLine {
                    if (it.endsWith(".m4a")) {
                        masterPlaylist.appendLine(
                            "${BASE_URL + EndPoints.PlaySong.route}?chunk=${pair.first}/${it}"
                        )
                    } else masterPlaylist.appendLine(it)
                }

                call.respondText(masterPlaylist.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
            }
        }
    }
}

fun Route.getSongSegment(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.PlaySong.route) {
            get {
                val chunk =
                    call.parameters["chunk"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                respondAudioSegment(File(PLAYLIST + chunk))
            }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondAudioSegment(file: File) {
    // Set response headers
    call.response.header(HttpHeaders.ContentDisposition, "inline; filename=${file.name}")
    call.response.header(HttpHeaders.ContentType, "mpeg")

    // Stream the file content
    call.respondOutputStream {
        file.inputStream().copyTo(this)
    }
}