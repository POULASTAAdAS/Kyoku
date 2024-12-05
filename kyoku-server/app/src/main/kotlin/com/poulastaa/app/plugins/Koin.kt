package com.poulastaa.app.plugins

import com.poulastaa.app.di.provideJedisPool
import com.poulastaa.auth.data.di.provideAuthService
import com.poulastaa.core.data.di.provideUserDatabase
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(app: Application) {
    install(Koin) {
        modules(
            provideJedisPool(),
            provideUserDatabase(),
            provideAuthService()
        )
    }
}