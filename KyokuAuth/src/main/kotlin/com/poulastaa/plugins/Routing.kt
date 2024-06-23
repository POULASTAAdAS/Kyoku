package com.poulastaa.plugins

import com.poulastaa.data.model.session.UserSession
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File

fun Application.configureRouting() {
    routing {
        sessionInterceptor()

        staticFiles(
            remotePath = ".well-known",
            dir = File("certs")
        )
    }
}

private fun Route.sessionInterceptor() {
    intercept(ApplicationCallPipeline.Call) {
        call.sessions.get<UserSession>()?.let {
            call.sessions.set(it)
        }
    }
}