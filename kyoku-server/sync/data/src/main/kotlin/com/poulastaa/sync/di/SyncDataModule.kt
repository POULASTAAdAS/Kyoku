package com.poulastaa.sync.di

import com.poulastaa.sync.domain.repository.SynRepository
import com.poulastaa.sync.repository.SyncRepositoryService
import org.koin.dsl.module

fun provideSyncDataService() = module {
    single<SynRepository> {
        SyncRepositoryService(get())
    }
}