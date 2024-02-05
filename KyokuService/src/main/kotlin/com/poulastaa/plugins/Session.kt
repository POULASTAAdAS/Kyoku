package com.poulastaa.plugins

import com.poulastaa.data.model.auth.GoogleUserSession
import com.poulastaa.data.model.auth.PasskeyUserSession
import com.poulastaa.data.repository.SessionStorageImpl
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

        cookie<PasskeyUserSession>(
            name = Constants.SESSION_NAME_PASSKEY,
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