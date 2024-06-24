package com.poulastaa.di

import com.poulastaa.data.repository.JWTRepositoryImpl
import com.poulastaa.domain.repository.JWTRepository
import io.ktor.server.application.*
import org.koin.dsl.module

fun provideJWTRepository(call: Application) = module {
    single<JWTRepository> {
        JWTRepositoryImpl(call)
    }
}