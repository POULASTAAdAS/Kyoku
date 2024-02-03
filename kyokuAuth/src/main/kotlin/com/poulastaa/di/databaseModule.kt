package com.poulastaa.di

import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.data.repository.UserServiceRepositoryImpl
import com.poulastaa.data.repository.jwt.JWTRepositoryImpl
import com.poulastaa.data.repository.user_db.EmailAuthUserRepositoryImpl
import com.poulastaa.data.repository.user_db.GoogleAuthUserRepositoryImpl
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.jwt.JWTRepository
import com.poulastaa.domain.repository.user_db.EmailAuthUserRepository
import io.ktor.server.application.*
import org.koin.dsl.module

fun provideJWTRepo(call: Application) = module {
    single<JWTRepository> {
        JWTRepositoryImpl(call)
    }
}

fun provideDatabaseRepo() = module {
    single<EmailAuthUserRepository> {
        EmailAuthUserRepositoryImpl()
    }
    single<GoogleAuthUserRepository> {
        GoogleAuthUserRepositoryImpl()
    }
}

fun provideService() = module {
    single<UserServiceRepository> {
        UserServiceRepositoryImpl(
            emailAuthUser = get(),
            jwtRepository = get(),
            googleAuthUser = get()
        )
    }
}
