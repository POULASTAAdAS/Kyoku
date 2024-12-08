package com.poulastaa.app.plugins

import com.poulastaa.auth.domain.repository.AuthRepository
import com.pouluastaa.auth.network.routes.auth
import com.pouluastaa.auth.network.routes.unAuthorized
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val authRepository: AuthRepository by inject()

    routing {
        auth(authRepository)

        unAuthorized()

        staticFiles(
            remotePath = ".well-known",
            dir = File("assets/certs")
        )
    }
}