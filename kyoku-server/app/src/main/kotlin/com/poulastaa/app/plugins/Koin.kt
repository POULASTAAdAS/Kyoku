package com.poulastaa.app.plugins

import com.poulastaa.auth.data.di.provideAuthService
import com.poulastaa.auth.data.di.provideJWTService
import com.poulastaa.core.data.di.provideGsonService
import com.poulastaa.core.database.di.provideJedisPoolService
import com.poulastaa.core.database.di.provideUserDatabaseService
import com.poulastaa.notification.data.di.provideNotificationService
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    issuer: String,
    audience: String,
    privateKeyPayload: String,
) {
    install(Koin) {
        modules(
            provideGsonService(),
            provideJedisPoolService(),
            provideUserDatabaseService(),
            provideJWTService(
                issuer = issuer,
                audience = audience,
                privateKeyPayload = privateKeyPayload
            ),
            provideAuthService(),
            provideNotificationService()
        )
    }
}