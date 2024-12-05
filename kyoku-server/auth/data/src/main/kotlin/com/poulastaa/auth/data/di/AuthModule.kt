package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.repository.AuthenticationService
import com.poulastaa.auth.domain.repository.AuthRepository
import org.koin.dsl.module

fun provideAuthService() = module {
    single<AuthRepository> {
        AuthenticationService(
            db = get()
        )
    }
}