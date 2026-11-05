package com.poulastaa.common.database.di

import androidx.room.Room
import com.poulastaa.common.database.DATABASE_NAME
import com.poulastaa.common.database.KyokuDatabase
import com.poulastaa.common.database.buildKyokuDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val databaseModule: Module = module {
    single<KyokuDatabase> {
        Room.databaseBuilder<KyokuDatabase>(
            name = "${documentDirectory()}/$DATABASE_NAME",
        ).buildKyokuDatabase()
    }
    single { get<KyokuDatabase>().authDao }
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
