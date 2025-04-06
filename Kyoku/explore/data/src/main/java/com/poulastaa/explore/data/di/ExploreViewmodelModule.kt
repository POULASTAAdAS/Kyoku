package com.poulastaa.explore.data.di

import com.poulastaa.core.domain.repository.LocalAllFromArtistDatasource
import com.poulastaa.core.domain.repository.LocalExploreAlbumDatasource
import com.poulastaa.explore.data.repository.OnlineFirstAllFromArtistRepository
import com.poulastaa.explore.data.repository.OnlineFirstExploreAlbumRepository
import com.poulastaa.explore.data.repository.OnlineFirstExploreArtistRepository
import com.poulastaa.explore.domain.repository.album.ExploreAlbumRepository
import com.poulastaa.explore.domain.repository.album.RemoteExploreAlbumDatasource
import com.poulastaa.explore.domain.repository.all_from_artist.AllFromArtistRepository
import com.poulastaa.explore.domain.repository.all_from_artist.RemoteAllFromArtistDatasource
import com.poulastaa.explore.domain.repository.artist.ExploreArtistRepository
import com.poulastaa.explore.domain.repository.artist.RemoteExploreArtistDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object ExploreViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideAllFromArtistRepository(
        remote: RemoteAllFromArtistDatasource,
        local: LocalAllFromArtistDatasource,
    ): AllFromArtistRepository = OnlineFirstAllFromArtistRepository(remote, local)

    @Provides
    @ViewModelScoped
    fun provideExploreAlbumRepository(
        remote: RemoteExploreAlbumDatasource,
        local: LocalExploreAlbumDatasource,
    ): ExploreAlbumRepository = OnlineFirstExploreAlbumRepository(remote, local)

    @Provides
    @ViewModelScoped
    fun provideExploreArtistRepository(
        remote: RemoteExploreArtistDatasource,
    ): ExploreArtistRepository = OnlineFirstExploreArtistRepository(remote)
}