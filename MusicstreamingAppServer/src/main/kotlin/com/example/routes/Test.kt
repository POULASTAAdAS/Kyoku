package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.io.File

fun Route.getMasterPlaylist() {
    get("/master.m3u8") {
        val masterPlaylist = StringBuilder()

        val file = File("D://temp/musicDBScripts/Tum_Hi_Ho_master.m3u8")

        file.forEachLine {
            if (it.endsWith(".m3u8")) {
                masterPlaylist.appendLine("http://192.168.0.105:8080/playlist?playlist=${it}")
            } else masterPlaylist.appendLine(it)
        }

        call.respondText(masterPlaylist.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
    }
}

fun Route.getPlaylist() {
    get("/playlist{playlist}") {
        val playlist = call.parameters["playlist"]!!

        call.application.log.debug("playlist:  $playlist")

        val quality = playlist.split("/")[0]

        val playlistContent = StringBuilder()

        val file = File("D://temp/musicDBScripts/${playlist}")
//        "D://temp/musicDBScripts/128/playlistTum_Hi_Ho.m3u8"

        file.forEachLine {
            if (it.endsWith(".m4a")) playlistContent.appendLine("http://192.168.0.105:8080/audio?chunk=$quality/$it")
            else playlistContent.appendLine(it)
        }

        call.respondText(playlistContent.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
    }
}


fun Route.getAudio() {
    get("/audio{chunk}") {
        val chunk = call.parameters["chunk"]!!
        // 128/segment0Tum_Hi_Ho.m4a
        call.application.log.debug("playlist:  $chunk")

        val filePath = "D://temp/musicDBScripts/$chunk"

        respondAudioSegment(File(filePath))
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