package com.poulastaa.plugins

import com.poulastaa.di.provideDatabaseRepository
import com.poulastaa.di.provideJWTRepo
import com.poulastaa.di.provideSetupRepository
import com.poulastaa.di.provideUserRepository
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    application: Application,
) {
    install(Koin) {
        modules(
            provideJWTRepo(application),
            provideDatabaseRepository(),
            provideSetupRepository(),
            provideUserRepository()
        )
    }
}