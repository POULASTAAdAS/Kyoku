package com.example.plugins

import com.example.data.model.GoogleUserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<GoogleUserSession>(
            name = "GOOGLE_USER_SESSION",
            SessionStorageMemory()
        )
    }
}