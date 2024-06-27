package com.poulastaa.plugins

import com.poulastaa.data.repository.SessionStorageImpl
import com.poulastaa.domain.model.GoogleUserSession
import com.poulastaa.utils.Constants
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<GoogleUserSession>(
            name = Constants.SESSION_NAME_GOOGLE,
            SessionStorageImpl()
        ) {
            transform(
                SessionTransportTransformerEncrypt(
                    hex(System.getenv("sessionEncryptionKey")),
                    hex(System.getenv("sessionSecretKey"))
                )
            )

            cookie.maxAgeInSeconds = Constants.DEFAULT_SESSION_MAX_AGE
        }
    }
}