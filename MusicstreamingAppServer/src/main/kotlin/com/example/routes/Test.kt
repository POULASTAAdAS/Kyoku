package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.getSong() {
    get("/song") {
        val mp3File = File("F://songs//Aashiqui 2//response.mp3")
        if (mp3File.exists()) {
//            val rangeHeader = call.request.header("Range")
//            val range = parseRangeHeader(rangeHeader!!, mp3File.length())
//            call.response.header(HttpHeaders.ContentRange, "bytes ${range?.first}-${range?.last}/${mp3File.length()}")
//            call.response.header(HttpHeaders.ContentType, "audio/mpeg")
//
//            call.respondFile(mp3File)

            val rangeHeader = call.request.header(HttpHeaders.Range)
            val range = parseRangeHeader(rangeHeader!!, mp3File.length())

            if (range != null) {
                call.respond(LocalFileContent(mp3File, contentType = ContentType.Any))
            } else {
                call.respond(LocalFileContent(mp3File))
            }
        } else {
            call.respond(HttpStatusCode.NotFound, "File not found")
        }
    }
}


fun parseRangeHeader(rangeHeader: String, fileSize: Long): LongRange? {
    val rangePattern = Regex("bytes=(\\d+)-(\\d*)")

    val matchResult = rangePattern.matchEntire(rangeHeader)
    return if (matchResult != null) {
        val start = matchResult.groups[1]?.value?.toLong() ?: 0
        val end = matchResult.groups[2]?.value?.toLong() ?: (fileSize - 1)
        start..end
    } else {
        null
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