package com.poulastaa.plugins

import com.poulastaa.data.model.auth.google.GoogleUserSession
import com.poulastaa.data.model.auth.passkey.PasskeyUserSession
import com.poulastaa.data.repository.SessionStorageImpl
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import com.poulastaa.utils.Constants.SESSION_NAME_GOOGLE
import com.poulastaa.utils.Constants.SESSION_NAME_PASSKEY
import com.poulastaa.utils.Constants.DEFAULT_SESSION_MAX_AGE

fun Application.configureSession() {
    install(Sessions) {
        cookie<GoogleUserSession>(
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

        cookie<PasskeyUserSession>(
            name = SESSION_NAME_PASSKEY,
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