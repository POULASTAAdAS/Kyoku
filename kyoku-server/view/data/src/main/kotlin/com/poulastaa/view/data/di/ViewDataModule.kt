package com.poulastaa.view.data.di

import com.poulastaa.view.data.repository.ViewRepositoryService
import com.poulastaa.view.domain.repository.ViewRepository
import org.koin.dsl.module

fun provideViewDataService() = module {
    single<ViewRepository> {
        ViewRepositoryService(get())
    }
}
