package com.poulastaa.add.data.di

import com.poulastaa.add.data.repository.OnlineFirstAddAlbumRepository
import com.poulastaa.add.data.repository.OnlineFirstAddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.AddAlbumRepository
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.RemoteAddAlbumDatasource
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.core.domain.repository.LocalAddAlbumDatasource
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

    @Provides
    @ViewModelScoped
    fun provideAddAlbumRepository(
        remote: RemoteAddAlbumDatasource,
        local: LocalAddAlbumDatasource,
        scope: CoroutineScope,
    ): AddAlbumRepository = OnlineFirstAddAlbumRepository(remote, local, scope)
}