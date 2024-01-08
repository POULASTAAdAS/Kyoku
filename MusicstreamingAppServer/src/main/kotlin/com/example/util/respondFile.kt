package com.example.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.launch
import java.io.File

fun PipelineContext<Unit, ApplicationCall>.respondFile(file: File) {
    call.response.header(HttpHeaders.ContentDisposition, "inline; filename=${file.name}")
    call.response.header(HttpHeaders.ContentType, ContentType.Image.Any.contentType)

    launch {
        call.respondOutputStream {
            file.inputStream().copyTo(this)
        }
    }
}