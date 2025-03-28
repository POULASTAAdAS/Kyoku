package com.poulastaa.search.data.di

import com.poulastaa.search.data.repository.ArtistPagingRepositoryService
import com.poulastaa.search.repository.ArtistPagingRepository
import org.koin.dsl.module

fun provideArtistPagerDataService() = module {
    single<ArtistPagingRepository> {
        ArtistPagingRepositoryService(get())
    }
}