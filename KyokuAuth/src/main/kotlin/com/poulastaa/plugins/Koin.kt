package com.poulastaa.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin


fun Application.configureKoin(
    application: Application
) {
    install(Koin) {
        modules(

        )
    }
}