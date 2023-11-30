package com.example.plugins

import com.example.routes.getCoverImage
import com.example.routes.getSong
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        getSong()
        getCoverImage()
    }
}