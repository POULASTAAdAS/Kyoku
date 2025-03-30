package com.poulastaa.item.data.di

import com.poulastaa.item.data.repository.ItemRepositoryService
import com.poulastaa.item.domain.repository.ItemRepository
import org.koin.dsl.module

fun provideItemDataService() = module {
    single<ItemRepository> {
        ItemRepositoryService(get())
    }
}