package com.poulastaa.plugins

import com.poulastaa.data.model.GoogleUserSession
import com.poulastaa.data.repository.SessionStorageImpl
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import com.poulastaa.utils.Constants.SESSION_NAME

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