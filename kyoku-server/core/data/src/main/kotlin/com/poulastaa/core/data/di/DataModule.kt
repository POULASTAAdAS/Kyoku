package com.poulastaa.core.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.poulastaa.core.data.usecase.LocalDateAdapter
import org.koin.dsl.module
import java.time.LocalDate

fun provideGsonService() = module {
    single<Gson> {
        GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()
    }
}