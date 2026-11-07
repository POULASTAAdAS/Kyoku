package com.poulastaa.common.database.di

import androidx.room.Room
import com.poulastaa.common.database.DATABASE_NAME
import com.poulastaa.common.database.KyokuDatabase
import com.poulastaa.common.database.buildKyokuDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

private const val APP_DIRECTORY = ".kyoku"

actual val databaseModule: Module = module {
    single<KyokuDatabase> {
        val directory = File(System.getProperty("user.home"), APP_DIRECTORY).apply { mkdirs() }
        Room.databaseBuilder<KyokuDatabase>(
            name = File(directory, DATABASE_NAME).absolutePath,
        ).buildKyokuDatabase()
    }
    single { get<KyokuDatabase>().authDao }
    single { get<KyokuDatabase>().songDao }
    single { get<KyokuDatabase>().playlistDao }
}
