package com.poulastaa.common.database.di

import com.poulastaa.common.database.preferences.DataStoreAppPreferences
import com.poulastaa.common.domain.preferences.AppPreferences
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect val platformDataStoreModule: Module

val preferencesModule: Module = module {
    includes(platformDataStoreModule)
    singleOf(::DataStoreAppPreferences) bind AppPreferences::class
}
