package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.io.File

fun Route.getSong() {
    get("/playlist.m3u8") {
        val listOfIndex = listOf(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
        )

        val playlistContent = StringBuilder()

        val file = File("F://db/320/chal-ghar-chale-(slowed+reverb)/chal-ghar-chale-(slowed+reverb).m3u8")

        file.forEachLine {
            if (it.endsWith(".m4a")) {
//                var dropSegment = it.drop(7)
//
//                dropSegment = if (dropSegment.first() in listOfIndex && dropSegment[1] in listOfIndex)
//                    "${dropSegment.first()}${dropSegment[1]}"
//                else dropSegment.first().toString()


                playlistContent.appendLine("http://192.168.0.105:8080/audio?chunk=$it")
            } else
                playlistContent.appendLine(it)
        }

        call.respondText(playlistContent.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
    }

    get("/audio{chunk}") {
        var chunk = call.parameters["chunk"]!!
        chunk = chunk.replace(' ', '+')

        val filePath = "F://db/320/chal-ghar-chale-(slowed+reverb)/${chunk}" // chal-ghar-chale-(slowed reverb).mp3-segment0.m4a
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


fun Route.getCoverImage() {
    get("/coverImage") {
        val imageFile =
            File("F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg") // Replace with the actual path to your image file
        if (imageFile.exists()) {
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "Aashiqui_2.jpg")
                    .toString()
            )
            call.respondFile(imageFile)
        } else {
            call.respond(HttpStatusCode.NotFound, "File not found")
        }
    }
}