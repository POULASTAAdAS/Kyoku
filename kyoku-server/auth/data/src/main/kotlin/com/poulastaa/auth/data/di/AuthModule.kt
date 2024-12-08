package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.repository.AuthenticationService
import com.poulastaa.auth.data.repository.EmailVerificationUserCase
import com.poulastaa.auth.data.repository.JWTRepositoryService
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.domain.repository.JWTRepository
import org.koin.dsl.module

fun provideAuthService() = module {
    single<EmailVerificationUserCase> {
        EmailVerificationUserCase()
    }

    single<AuthRepository> {
        AuthenticationService(
            db = get(),
            emailValidator = get(),
            jwt = get(),
        )
    }
}

fun provideJWTService(
    issuer: String,
    audience: String,
    privateKeyPayload: String,
) = module {
    single<JWTRepository> {
        JWTRepositoryService(
            issuer = issuer,
            audience = audience,
            privateKeyPayload = privateKeyPayload,
        )
    }
}