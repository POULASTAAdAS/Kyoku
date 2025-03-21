package com.poulastaa.main.data.di

import android.content.Context
import com.poulastaa.core.domain.repository.LocalWorkDatasource
import com.poulastaa.main.data.repository.work.DaggerHiltWorkRepository
import com.poulastaa.main.data.repository.work.SyncLibraryWorker
import com.poulastaa.main.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.main.domain.repository.work.SyncLibraryScheduler
import com.poulastaa.main.domain.repository.work.WorkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MainDataAppModule {
    @Provides
    @Singleton
    fun provideWorkRepository(
        local: LocalWorkDatasource,
        remote: RemoteWorkDatasource,
        scope: CoroutineScope,
    ): WorkRepository = DaggerHiltWorkRepository(
        local = local,
        remote = remote,
        scope = scope
    )

    @Provides
    @Singleton
    fun provideSyncLibraryWorker(
        @ApplicationContext context: Context,
        scope: CoroutineScope,
    ): SyncLibraryScheduler = SyncLibraryWorker(
        context = context,
        scope = scope
    )
}