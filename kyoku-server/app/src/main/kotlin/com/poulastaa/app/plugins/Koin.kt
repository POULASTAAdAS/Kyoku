package com.poulastaa.app.plugins

import com.poulastaa.auth.data.di.provideAuthService
import com.poulastaa.auth.data.di.provideJWTService
import com.poulastaa.core.data.di.provideGsonService
import com.poulastaa.core.database.di.provideJedisPoolService
import com.poulastaa.core.database.di.provideCoreDatabaseService
import com.poulastaa.notification.data.di.provideNotificationService
import com.poulastaa.user.data.di.provideUserDataRepositoryService
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    issuer: String,
    audience: String,
    privateKeyPayload: String,
    redisHost: String,
    redisPort: Int,
    redisPassword: String,
) {
    install(Koin) {
        modules(
            provideGsonService(),
            provideJedisPoolService(
                redisHost = redisHost,
                redisPort = redisPort,
                redisPassword = redisPassword
            ),
            provideCoreDatabaseService(),
            provideJWTService(
                issuer = issuer,
                audience = audience,
                privateKeyPayload = privateKeyPayload
            ),
            provideAuthService(),
            provideNotificationService(),
            provideUserDataRepositoryService()
        )
    }
}