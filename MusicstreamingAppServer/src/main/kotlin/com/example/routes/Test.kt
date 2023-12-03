package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.io.File

fun Route.getSong() {
//    get("/song") {
////        call.respondFile(File("F://songs//Aashiqui 2//Tum_Hi_Ho.m3u8"))
//        val playlistContent = buildString {
//            appendLine("#EXTM3U")
//            appendLine("#EXT-X-VERSION:3")
//            appendLine("#EXT-X-TARGETDURATION:0")
//            appendLine("#EXT-X-MEDIA-SEQUENCE:0")
//            appendLine("#EXTINF:0.000011,")
//            appendLine("http://192.168.0.105:8080/audio?index=1")
//            appendLine("#EXT-X-ENDLIST")
//        }
//
//        call.respondText(playlistContent, ContentType.parse("application/vnd.apple.mpegurl"))
//    }

//    get("/audio{index}") {
//        val index = call.parameters["index"] ?: return@get
//
//        println(index)
//
//        val filePath = "F://songs//Aashiqui 2//file0.m4a"
//
//        respondAudioSegment(File(filePath))
//    }


    get("/song.m3u8") {
        val listOfIndex = listOf(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
        )

        val playlistContent = StringBuilder()

        val file = File("G://temp//Tum_Hi_Ho_Playlist.m3u8")

        file.forEachLine {
            if (it.endsWith(".m4a")) {
                var dropSegment = it.drop(7)

                dropSegment = if (dropSegment.first() in listOfIndex && dropSegment[1] in listOfIndex)
                    "${dropSegment.first()}${dropSegment[1]}"
                else dropSegment.first().toString()


                playlistContent.appendLine("http://192.168.0.105:8080/audio?index=$dropSegment")
            } else
                playlistContent.appendLine(it)
        }

        call.respondText(playlistContent.toString(), ContentType.parse("application/vnd.apple.mpegurl"))
        println()
        println()
        println()
        println()
        println()
    }

    get("/audio{index}") {
        val index = call.parameters["index"]!!
        println(index)

        val filePath = "G://temp////segment${index}ForTum_Hi_Ho.m4a" // segment26ForTum_Hi_Ho.m4a
        respondAudioSegment(File(filePath))

        println()
        println()
        println()
        println()
        println()
        println()
        println()
        println()
        println()
        println()
        println()
        println()
        println()
        println()
    }
}


suspend fun PipelineContext<Unit, ApplicationCall>.respondAudioSegment(file: File) {
    // Set response headers
    call.response.header(HttpHeaders.ContentDisposition, "inline; filename=${file.name}")
    call.response.header(HttpHeaders.ContentType, ContentType.Audio.MPEG.toString())

    // Stream the file content
//    call.respondOutputStream {
//        file.inputStream().copyTo(this)
//    }
    call.respondFile(file)
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