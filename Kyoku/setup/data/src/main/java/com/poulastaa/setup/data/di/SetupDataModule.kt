package com.poulastaa.setup.data.di

import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
import com.poulastaa.setup.data.repository.OnlineFirstImportPlaylistRepository
import com.poulastaa.setup.data.repository.UseCaseValidatePlaylistLink
import com.poulastaa.setup.domain.repository.import_playlist.ImportPlaylistRepository
import com.poulastaa.setup.domain.repository.import_playlist.RemoteImportPlaylistDatasource
import com.poulastaa.setup.domain.repository.import_playlist.SpotifyPlaylistLinkValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SetupDataModule {
    @Provides
    @ViewModelScoped
    fun provideSpotifyLinkValidator(): SpotifyPlaylistLinkValidator = UseCaseValidatePlaylistLink()

    @Provides
    @ViewModelScoped
    fun provideImportPlaylistRepository(
        local: LocalImportPlaylistDatasource,
        remote: RemoteImportPlaylistDatasource,
    ): ImportPlaylistRepository = OnlineFirstImportPlaylistRepository(
        local = local,
        remote = remote
    )
}