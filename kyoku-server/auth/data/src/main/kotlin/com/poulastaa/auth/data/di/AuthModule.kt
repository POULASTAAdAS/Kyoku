package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.repository.AuthenticationService
import com.poulastaa.auth.data.repository.EmailVerificationUserCase
import com.poulastaa.auth.domain.repository.AuthRepository
import org.koin.dsl.module

fun provideAuthService() = module {
    single<EmailVerificationUserCase> {
        EmailVerificationUserCase()
    }

    single<AuthRepository> {
        AuthenticationService(
            db = get(),
            emailValidator = get()
        )
    }
}