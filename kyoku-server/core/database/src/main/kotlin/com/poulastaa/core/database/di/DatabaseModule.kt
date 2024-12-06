package com.poulastaa.core.database.di

import com.poulastaa.core.database.user.repository.ExposedLocalAuthDatasource
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import org.koin.dsl.module

fun provideUserDatabase() = module {
    single<LocalAuthDatasource> {
        ExposedLocalAuthDatasource(get())
    }
}