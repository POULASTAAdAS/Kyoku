package com.example.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.RandomAccessFile
import kotlin.math.min

fun Route.getSong() {
    get("/song") {
        val mp3File = File("F://songs//Aashiqui 2//Tum Hi Ho.mp3")
        if (mp3File.exists()) {
            val rangeHeader = call.request.header("Range")
            val range = parseRangeHeader(rangeHeader!!, mp3File.length())
            call.response.header(HttpHeaders.ContentRange, "bytes ${range?.first}-${range?.last}/${mp3File.length()}")

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "Tum Hi Ho.mp3")
                    .toString()
            )
            call.response.header(HttpHeaders.ContentType, "audio/mpeg")

            call.respondFile(mp3File)
        } else {
            call.respond(HttpStatusCode.NotFound, "File not found")
        }
    }
}


suspend fun PipelineContext<Unit, ApplicationCall>.respond(file: File, range: LongRange) {
    val randomAccessFile = withContext(Dispatchers.IO) {
        RandomAccessFile(file, "r")
    }

    val contentLength = (range.last - range.first) + 1

    try {
        call.response.header(HttpHeaders.ContentRange, "bytes ${range.first}-${range.last}/${file.length()}")
        call.response.header(HttpHeaders.ContentType, "audio/mpeg")
        call.response.header(HttpHeaders.ContentLength, contentLength.toString())

        withContext(Dispatchers.IO) {
            call.respondOutputStream {
                randomAccessFile.seek(range.first)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                var bytesRemaining = contentLength
                while (bytesRemaining > 0) {
                    bytesRead = randomAccessFile.read(buffer, 0, min(buffer.size, bytesRemaining.toInt()))
                    if (bytesRead < 0) break
                    write(buffer, 0, bytesRead)
                    bytesRemaining -= bytesRead
                }
            }
        }
    } finally {
        withContext(Dispatchers.IO) {
            randomAccessFile.close()
        }
    }
}

fun parseRangeHeader(rangeHeader: String, fileSize: Long): LongRange? {
    val rangePattern = Regex("""bytes=(\d+)-(\d*)""")

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