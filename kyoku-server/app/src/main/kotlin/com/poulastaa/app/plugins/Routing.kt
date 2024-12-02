package com.poulastaa.app.plugins

import com.pouluastaa.auth.network.routes.auth
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        auth()
    }
}