package com.poulastaa.common.database.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.poulastaa.common.database.preferences.DATA_STORE_FILE_NAME
import com.poulastaa.common.database.preferences.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

private const val APP_DIRECTORY = ".kyoku"

internal actual val platformDataStoreModule: Module = module {
    single<DataStore<Preferences>> {
        val directory = File(System.getProperty("user.home"), APP_DIRECTORY).apply { mkdirs() }
        createDataStore { File(directory, DATA_STORE_FILE_NAME).absolutePath }
    }
}
