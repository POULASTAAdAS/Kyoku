package com.poulastaa.main.data.di

import android.content.Context
import com.poulastaa.core.domain.repository.LocalRefreshDatasource
import com.poulastaa.core.domain.repository.LocalWorkDatasource
import com.poulastaa.main.data.repository.work.DaggerHiltRefreshRepository
import com.poulastaa.main.data.repository.work.DaggerHiltWorkRepository
import com.poulastaa.main.data.repository.work.RefreshSchedulerAlarmWorker
import com.poulastaa.main.data.repository.work.SyncLibrarySchedulerWorker
import com.poulastaa.main.domain.repository.work.RefreshRepository
import com.poulastaa.main.domain.repository.work.RefreshScheduler
import com.poulastaa.main.domain.repository.work.RemoteRefreshDatasource
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
    ): SyncLibraryScheduler = SyncLibrarySchedulerWorker(
        context = context,
        scope = scope
    )

    @Provides
    @Singleton
    fun provideRefreshRepository(
        local: LocalRefreshDatasource,
        remote: RemoteRefreshDatasource,
        scope: CoroutineScope,
    ): RefreshRepository = DaggerHiltRefreshRepository(local, remote, scope)

    @Provides
    @Singleton
    fun provideRefreshScheduler(
        @ApplicationContext context: Context,
    ): RefreshScheduler = RefreshSchedulerAlarmWorker(context)
}