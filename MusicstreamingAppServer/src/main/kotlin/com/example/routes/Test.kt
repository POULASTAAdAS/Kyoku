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

        val file = File("E:/songdb/master/Akela_Divine/Akela_Divine_master.m3u8")

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
        val file = File(playlist)

        val rootPath = getBasePath(playlist)

        val playlistContent = StringBuilder()

        file.forEachLine {
            if (it.endsWith(".m4a")) playlistContent.appendLine("http://192.168.0.105:8080/audio?chunk=$rootPath$it")
            else playlistContent.appendLine(it)
        }

        call.respondText(playlistContent.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
    }
}


fun Route.getAudio() {
    get("/audio{chunk}") {
        val chunk = call.parameters["chunk"]!!

        respondAudioSegment(File(chunk))
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

private fun getBasePath(fullPath: String): String = fullPath.replace(Regex("playlist.*") , "")