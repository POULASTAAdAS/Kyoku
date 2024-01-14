package com.example.plugins

import com.example.data.model.GoogleUserSession
import com.example.data.repository.SessionStorageImpl
import com.example.util.Constants.DEFAULT_SESSION_MAX_AGE
import com.example.util.Constants.SESSION_NAME
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<GoogleUserSession>(
            name = SESSION_NAME,
            SessionStorageImpl()
        ) {
            transform(
                SessionTransportTransformerEncrypt(
                    hex(System.getenv("sessionEncryptionKey")),
                    hex(System.getenv("sessionSecretKey"))
                )
            )

            cookie.maxAgeInSeconds = DEFAULT_SESSION_MAX_AGE
        }
    }
}