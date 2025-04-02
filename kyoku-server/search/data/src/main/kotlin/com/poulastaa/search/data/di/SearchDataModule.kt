package com.poulastaa.search.data.di

import com.poulastaa.search.data.repository.PagingRepositoryService
import com.poulastaa.search.repository.PagingRepository
import org.koin.dsl.module

fun provideArtistPagerDataService() = module {
    single<PagingRepository> {
        PagingRepositoryService(get())
    }
}