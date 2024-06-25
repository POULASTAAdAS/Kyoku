package com.poulastaa.di

import com.poulastaa.data.repository.AuthRepositoryImpl
import com.poulastaa.data.repository.JWTRepositoryImpl
import com.poulastaa.data.repository.ServiceRepositoryImpl
import com.poulastaa.domain.repository.AuthRepository
import com.poulastaa.domain.repository.JWTRepository
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.server.application.*
import org.koin.dsl.module

fun provideJWTRepository(call: Application) = module {
    single<JWTRepository> {
        JWTRepositoryImpl(call)
    }
}

fun provideRepository() = module {
    single<AuthRepository> {
        AuthRepositoryImpl()
    }

    single<ServiceRepository> {
        ServiceRepositoryImpl(
            authRepo = get(),
            jwtRepo = get()
        )
    }
}