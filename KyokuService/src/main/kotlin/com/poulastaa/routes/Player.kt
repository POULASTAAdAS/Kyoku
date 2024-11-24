package com.poulastaa.routes

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.Constants.CURRENT_PROJECT_FOLDER
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR
import com.poulastaa.utils.Constants.PLAYLIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.io.File

fun Route.getMasterPlaylist(
    service: ServiceRepository,
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.PlaySongMaster.route) {
            get {
                val playlist = call.parameters["master"]
                    ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val file = File(CURRENT_PROJECT_FOLDER + MASTER_PLAYLIST_ROOT_DIR + playlist)

                val masterPlaylist = StringBuilder()
                file.forEachLine {
                    if (it.endsWith(".m3u8")) {
                        masterPlaylist.appendLine(
                            "${System.getenv("SERVICE_URL") + EndPoints.PlaySongPlaylist.route}?playlist=${
                                it.removePrefix("G:/songs/").replace("playlist_", "playlist")
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
    service: ServiceRepository,
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.PlaySongPlaylist.route) {
            get {
                val playlist =
                    call.parameters["playlist"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                val pair =
                    if (playlist.contains("128")) playlist.split('/')[1] to File("$CURRENT_PROJECT_FOLDER/$playlist")
                    else playlist.split('/')[1] to File("$CURRENT_PROJECT_FOLDER/$playlist")

                val masterPlaylist = StringBuilder()
                pair.second.forEachLine {
                    if (it.endsWith(".m4a")) {
                        masterPlaylist.appendLine(
                            "${System.getenv("SERVICE_URL") + EndPoints.PlaySong.route}?chunk=${pair.first}/${it}"
                        )
                    } else masterPlaylist.appendLine(it)
                }

                call.respondText(masterPlaylist.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
            }
        }
    }
}

fun Route.getSongSegment(
    service: ServiceRepository,
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.PlaySong.route) {
            get {
                val chunk =
                    call.parameters["chunk"] ?: return@get call.respondRedirect(EndPoints.UnAuthorised.route)

                respondAudioSegment(File(CURRENT_PROJECT_FOLDER + PLAYLIST + chunk.take(4) + extractFileNameFromChunk(chunk) +  chunk.substringAfterLast("/")))
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

fun extractFileNameFromChunk(chunkValue: String): String {
    val fileWithExtension = chunkValue.substringAfterLast("/")  // Get the file name with extension
    var fileName = fileWithExtension.substringBeforeLast(".")   // Remove the extension

    // Regular expression to match 'segment' followed by any digits
    val regex = "^segment\\d+".toRegex()

    // Remove the 'segment' prefix if it exists
    fileName = fileName.replace(regex, "")

    return "$fileName/"
}
