package com.poulastaa.common.database.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.poulastaa.common.database.preferences.DATA_STORE_FILE_NAME
import com.poulastaa.common.database.preferences.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual val platformDataStoreModule: Module = module {
    single<DataStore<Preferences>> {
        createDataStore { "${documentDirectory()}/$DATA_STORE_FILE_NAME" }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
