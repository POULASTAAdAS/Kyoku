package com.example.plugins

import com.example.di.provideDatabaseRepo
import com.example.di.provideJWTRepo
import com.example.di.provideService
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    application: Application
) {
    install(Koin) {
        modules(
            provideJWTRepo(application),
            provideDatabaseRepo(),
            provideService()
        )
    }
}