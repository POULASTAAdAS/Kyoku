package com.poulastaa.play.data.di

import android.content.Context
import com.poulastaa.core.domain.repository.work.LocalWorkDatasource
import com.poulastaa.core.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.core.domain.repository.work.WorkRepository
import com.poulastaa.play.data.OnlineWorkRepository
import com.poulastaa.play.data.work.SyncLibraryWorkerScheduler
import com.poulastaa.play.domain.SyncLibraryScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayDataAppModule {
    @Provides
    @Singleton
    fun provideOnlineWorkRepository(
        local: LocalWorkDatasource,
        remote: RemoteWorkDatasource,
        applicationScope: CoroutineScope,
    ): WorkRepository = OnlineWorkRepository(
        local = local,
        remote = remote,
        applicationScope = applicationScope
    )

    @Provides
    @Singleton
    fun provideSyncLibraryWorkerScheduler(
        @ApplicationContext context: Context,
        applicationScope: CoroutineScope,
    ): SyncLibraryScheduler = SyncLibraryWorkerScheduler(
        context = context,
        applicationScope = applicationScope
    )
}