package com.poulastaa.di

import com.poulastaa.data.repository.JWTRepositoryImpl
import com.poulastaa.data.repository.KyokuDatabaseImpl
import com.poulastaa.data.repository.SetupRepositoryDatabaseImpl
import com.poulastaa.data.repository.UserRepositoryImpl
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.repository.JWTRepository
import com.poulastaa.domain.repository.SetupRepository
import com.poulastaa.domain.repository.UserRepository
import io.ktor.server.application.*
import org.koin.dsl.module

fun provideJWTRepo(call: Application) = module {
    single<JWTRepository> {
        JWTRepositoryImpl(call)
    }
}

fun provideDatabaseRepository() = module {
    single<DatabaseRepository> {
        KyokuDatabaseImpl()
    }
}

fun provideSetupRepository() = module {
    single<SetupRepository> {
        SetupRepositoryDatabaseImpl(
            kyokuDatabase = get()
        )
    }
}

fun provideUserRepository() = module {
    single<UserRepository> {
        UserRepositoryImpl(
            kyokuDatabase = get(),
            setupRepository = get()
        )
    }
}