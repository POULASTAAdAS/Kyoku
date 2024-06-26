package com.poulastaa.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCors() {
    install(CORS) {
//        allowHost("client-host", schemes = listOf("http", "https"))
        allowHeader(HttpHeaders.ContentType)
//        allowHeader(HttpHeaders.Authorization)
//        allowCredentials = true
        anyHost()
    }
}