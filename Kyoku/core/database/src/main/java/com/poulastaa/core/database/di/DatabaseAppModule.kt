package com.poulastaa.core.database.di

import android.content.Context
import androidx.room.Room
import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.PlayerDao
import com.poulastaa.core.database.dao.RecentHistoryDao
import com.poulastaa.core.database.dao.WorkDao
import com.poulastaa.core.database.repository.RoomLocalWorkDatasource
import com.poulastaa.core.domain.repository.work.LocalWorkDatasource
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
        klass = KyokuDatabase::class.java,
        name = "KyokuDatabase"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideCommonDao(
        database: KyokuDatabase,
    ): CommonDao = database.commonDao

    @Provides
    @Singleton
    fun provideWorkDao(
        database: KyokuDatabase,
    ): WorkDao = database.workDao

    @Provides
    @Singleton
    fun provideLocalWorkDatasource(
        commonDao: CommonDao,
        workDao: WorkDao,
    ): LocalWorkDatasource = RoomLocalWorkDatasource(
        commonDao = commonDao,
        workDao = workDao
    )

    @Provides
    @Singleton
    fun provideRecentHistoryDao(
        database: KyokuDatabase,
    ): RecentHistoryDao = database.recentHistoryDao

    @Provides
    @Singleton
    fun providePlayerDao(
        database: KyokuDatabase,
    ): PlayerDao = database.playerDao
}