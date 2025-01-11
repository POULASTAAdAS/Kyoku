package com.poulastaa.core.database.di

import android.content.Context
import androidx.room.Room
import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.RootDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDatabaseAppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): KyokuDatabase = Room.databaseBuilder(
        context = context,
        klass = KyokuDatabase::class.java,
        name = "AppDatabase"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideRootDao(
        database: KyokuDatabase,
    ): RootDao = database.rootDao
}