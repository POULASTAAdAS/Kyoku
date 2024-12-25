package com.poulastaa.app.plugins

import com.poulastaa.core.data.repository.RedisSessionStorageRepository
import com.poulastaa.core.domain.utils.Constants.DEFAULT_SESSION_MAX_AGE
import com.poulastaa.core.domain.utils.Constants.SESSION_NAME_GOOGLE
import com.poulastaa.core.network.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.koin.ktor.ext.get

fun Application.configureSession(
    sessionEncryptionKey: String,
    sessionSecretKey: String,
) {
    val repo: RedisSessionStorageRepository = get()

    install(Sessions) {
        cookie<UserSession>(
            name = SESSION_NAME_GOOGLE,
            storage = repo
        ) {
            transform(
                SessionTransportTransformerEncrypt(
                    hex(sessionEncryptionKey),
                    hex(sessionSecretKey)
                )
            )
            cookie.maxAgeInSeconds = DEFAULT_SESSION_MAX_AGE
        }
    }
}