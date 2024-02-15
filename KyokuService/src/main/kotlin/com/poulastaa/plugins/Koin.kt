package com.poulastaa.plugins

import com.poulastaa.di.provideDatabaseRepo
import com.poulastaa.di.provideDbUsers
import com.poulastaa.di.provideJWTRepo
import com.poulastaa.di.provideService
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    application: Application
) {
    install(Koin) {
        modules(
            provideJWTRepo(application),
            provideDatabaseRepo(),
            provideDbUsers(),
            provideService()
        )
    }
}