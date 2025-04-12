package com.poulastaa.add.data.di

import com.poulastaa.add.data.repository.OnlineFirstAddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.core.domain.repository.LocalAddSongToPlaylistDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object AddViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideAddSongToPlaylistRepository(
        remote: RemoteAddSongToPlaylistDatasource,
        local: LocalAddSongToPlaylistDatasource,
        scope: CoroutineScope,
    ): AddSongToPlaylistRepository = OnlineFirstAddSongToPlaylistRepository(remote, local, scope)
}