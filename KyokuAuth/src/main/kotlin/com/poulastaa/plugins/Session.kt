package com.poulastaa.plugins

import com.poulastaa.data.model.session.UserSession
import com.poulastaa.data.repository.SessionStorageImpl
import com.poulastaa.utils.Constants.SESSION_NAME_GOOGLE
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<UserSession>(
            name = SESSION_NAME_GOOGLE,
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