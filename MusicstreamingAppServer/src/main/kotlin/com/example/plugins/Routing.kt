package com.example.plugins

import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        getMasterPlaylist()
        getPlaylist()
        getAudio()
    }
}