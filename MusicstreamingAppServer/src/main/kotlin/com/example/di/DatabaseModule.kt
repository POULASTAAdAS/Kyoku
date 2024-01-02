package com.example.di

import com.example.data.repository.SongServiceImpl
import com.example.domain.repository.SongService
import org.koin.dsl.module

val dbModule = module {
    single<SongService> {
        SongServiceImpl()
    }
}