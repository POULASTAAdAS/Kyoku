package com.poulastaa.di

import com.poulastaa.data.repository.*
import com.poulastaa.domain.repository.*
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
        UserDatabaseRepository()
    }
}

fun provideHomeRepository() = module {
    single<HomeRepository> {
        HomeRepositoryImpl()
    }
}

fun provideServiceRepository() = module {
    single<ServiceRepository> {
        ServiceRepositoryImpl(
            kyokuRepo = get(),
            setupRepo = get(),
            userRepo = get(),
            homeRepo = get(),
            jwt = get()
        )
    }
}