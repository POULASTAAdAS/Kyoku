package com.poulastaa.common.database.di

import android.content.Context
import androidx.room.Room
import com.poulastaa.common.database.DATABASE_NAME
import com.poulastaa.common.database.KyokuDatabase
import com.poulastaa.common.database.buildKyokuDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseModule: Module = module {
    single<KyokuDatabase> {
        val context = get<Context>().applicationContext
        Room.databaseBuilder<KyokuDatabase>(
            context = context,
            name = context.getDatabasePath(DATABASE_NAME).absolutePath,
        ).buildKyokuDatabase()
    }
    single { get<KyokuDatabase>().authDao }
}
