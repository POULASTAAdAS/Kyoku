package com.poulastaa.core.database.di

import android.content.Context
import androidx.room.Room
import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.CommonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseAppModule {
    @Provides
    @Singleton
    fun provideKyokuDatabase(
        @ApplicationContext context: Context,
    ): KyokuDatabase = Room.databaseBuilder(
        context = context,
        KyokuDatabase::class.java,
        "KyokuDatabase"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideCommonDao(
        database: KyokuDatabase,
    ): CommonDao = database.commonDao
}