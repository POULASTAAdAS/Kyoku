package com.poulastaa.user.data.di

import com.poulastaa.user.data.repository.SetupRepositoryService
import com.poulastaa.user.domain.repository.SetupRepository
import org.koin.dsl.module

fun provideUserDataRepositoryService() = module {
    single<SetupRepository> {
        SetupRepositoryService(get())
    }
}