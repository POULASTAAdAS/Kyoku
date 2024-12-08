package com.poulastaa.app.plugins

import com.poulastaa.auth.data.di.provideAuthService
import com.poulastaa.auth.data.di.provideJWTService
import com.poulastaa.core.data.di.provideGson
import com.poulastaa.core.database.di.provideJedisPool
import com.poulastaa.core.database.di.provideUserDatabase
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    issuer: String,
    audience: String,
    privateKeyPayload: String,
) {
    install(Koin) {
        modules(
            provideGson(),
            provideJedisPool(),
            provideUserDatabase(),
            provideJWTService(
                issuer = issuer,
                audience = audience,
                privateKeyPayload = privateKeyPayload
            ),
            provideAuthService()
        )
    }
}