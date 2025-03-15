package com.poulastaa.view.data.di

import com.poulastaa.core.domain.repository.LocalViewArtistDatasource
import com.poulastaa.core.domain.repository.LocalViewOtherDatasource
import com.poulastaa.view.data.repository.OnlineFirstViewArtistRepository
import com.poulastaa.view.data.repository.OnlineFirstViewOtherRepository
import com.poulastaa.view.domain.repository.RemoteViewArtistDatasource
import com.poulastaa.view.domain.repository.RemoteViewOtherDatasource
import com.poulastaa.view.domain.repository.ViewArtistRepository
import com.poulastaa.view.domain.repository.ViewOtherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object ViewViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideViewArtistRepository(
        remote: RemoteViewArtistDatasource,
        local: LocalViewArtistDatasource,
    ): ViewArtistRepository = OnlineFirstViewArtistRepository(remote, local)

    @Provides
    @ViewModelScoped
    fun provideViewRepository(
        local: LocalViewOtherDatasource,
        remote: RemoteViewOtherDatasource,
        scope: CoroutineScope,
    ): ViewOtherRepository = OnlineFirstViewOtherRepository(
        local = local,
        remote = remote,
        scope = scope
    )
}