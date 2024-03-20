package com.poulastaa.di

import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.data.repository.UserServiceRepositoryImpl
import com.poulastaa.data.repository.jwt.JWTRepositoryImpl
import com.poulastaa.data.repository.login.LogInResponseRepositoryImpl
import com.poulastaa.data.repository.user_db.EmailAuthUserRepositoryImpl
import com.poulastaa.data.repository.user_db.GoogleAuthUserRepositoryImpl
import com.poulastaa.data.repository.user_db.PasskeyAuthUserRepositoryImpl
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.jwt.JWTRepository
import com.poulastaa.domain.repository.login.LogInResponseRepository
import com.poulastaa.domain.repository.user_db.EmailAuthUserRepository
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository
import io.ktor.server.application.*
import org.koin.dsl.module

fun provideJWTRepo(call: Application) = module {
    single<JWTRepository> {
        JWTRepositoryImpl(call)
    }
}

fun provideDatabaseRepo() = module {
    single<LogInResponseRepository> {
        LogInResponseRepositoryImpl()
    }

    single<EmailAuthUserRepository> {
        EmailAuthUserRepositoryImpl(
            loginRepository = get()
        )
    }
    single<GoogleAuthUserRepository> {
        GoogleAuthUserRepositoryImpl(
            loginRepository = get()
        )
    }
    single<PasskeyAuthUserRepository> {
        PasskeyAuthUserRepositoryImpl(
            loginRepository = get()
        )
    }
}

fun provideService() = module {
    single<UserServiceRepository> {
        UserServiceRepositoryImpl(
            jwtRepository = get(),
            emailAuthUser = get(),
            googleAuthUser = get(),
            passkeyAuthUser = get()
        )
    }
}
