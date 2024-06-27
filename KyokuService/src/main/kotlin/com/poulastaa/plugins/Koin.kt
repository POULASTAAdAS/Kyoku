package com.poulastaa.plugins

import com.poulastaa.di.provideJWTRepo
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    application: Application,
) {
    install(Koin) {
        modules(
            provideJWTRepo(application)
        )
    }
}