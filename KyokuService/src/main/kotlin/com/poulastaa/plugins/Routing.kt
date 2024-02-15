package com.poulastaa.plugins

import com.poulastaa.data.model.auth.GoogleUserSession
import com.poulastaa.data.model.auth.PasskeyUserSession
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.routes.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val service: UserServiceRepository by inject()

    routing {
        sessionInterceptor()

        getSpotifyPlaylist(service)
        getSongCover(service)

        storeBDate(service)

        getMasterPlaylist(service)

        unauthorized()

        staticFiles(
            remotePath = ".well-known",
            dir = File("certs")
        )
    }
}

private fun Route.sessionInterceptor() {
    intercept(ApplicationCallPipeline.Call) {
        call.sessions.get<GoogleUserSession>()?.let {
            call.sessions.set(it)
        }

        call.sessions.get<PasskeyUserSession>()?.let {
            call.sessions.set(it)
        }
    }
}