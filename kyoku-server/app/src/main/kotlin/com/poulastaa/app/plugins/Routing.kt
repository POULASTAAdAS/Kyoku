package com.poulastaa.app.plugins

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.routes.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val authRepository: AuthRepository by inject()

    routing {
        auth(authRepository)
        verifyEmail(authRepository)
        getJWTToken(authRepository)
        forgotPassword(authRepository)
        changePassword(authRepository)
        resetPassword(authRepository)

        unAuthorized()

        staticFiles(
            remotePath = ".well-known",
            dir = File("assets/certs")
        )

        staticFiles(
            remotePath = "images",
            dir = File("assets/images")
        )
    }
}