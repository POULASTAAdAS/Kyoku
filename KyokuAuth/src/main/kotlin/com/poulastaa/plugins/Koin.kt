package com.poulastaa.plugins

import com.poulastaa.di.provideJWTRepository
import com.poulastaa.di.provideRepository
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin


fun Application.configureKoin(
    application: Application,
) {
    install(Koin) {
        modules(
            provideJWTRepository(application),
            provideRepository()
        )
    }
}