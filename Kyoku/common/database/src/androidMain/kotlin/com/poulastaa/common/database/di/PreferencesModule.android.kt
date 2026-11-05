package com.poulastaa.common.database.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.poulastaa.common.database.preferences.DATA_STORE_FILE_NAME
import com.poulastaa.common.database.preferences.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDataStoreModule: Module = module {
    single<DataStore<Preferences>> {
        val context = get<Context>().applicationContext
        createDataStore { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
    }
}
