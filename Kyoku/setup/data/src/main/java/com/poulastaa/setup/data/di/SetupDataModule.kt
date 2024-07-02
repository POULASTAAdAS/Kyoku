package com.poulastaa.setup.data.di

import com.poulastaa.core.domain.b_date.BDateRepository
import com.poulastaa.core.domain.b_date.RemoteBDateDataSource
import com.poulastaa.core.domain.genre.GenreRepository
import com.poulastaa.core.domain.genre.RemoteGenreDataSource
import com.poulastaa.core.domain.get_spotify_playlist.LocalSpotifyDataSource
import com.poulastaa.core.domain.get_spotify_playlist.RemoteSpotifyDataSource
import com.poulastaa.core.domain.get_spotify_playlist.SpotifyRepository
import com.poulastaa.setup.data.OnlineBDateRepository
import com.poulastaa.setup.data.OnlineFirstSpotifyRepository
import com.poulastaa.setup.data.OnlineGenreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object SetupDataModule {
    @Provides
    @ViewModelScoped
    fun provideSpotifyRemoteDataSource(
        remote: RemoteBDateDataSource,
        applicationScope: CoroutineScope,
    ): BDateRepository = OnlineBDateRepository(
        remote = remote,
        applicationScope = applicationScope
    )

    @Provides
    @ViewModelScoped
    fun provideSpotifyLocalDataSource(
        local: LocalSpotifyDataSource,
        remote: RemoteSpotifyDataSource,
        applicationScope: CoroutineScope,
    ): SpotifyRepository = OnlineFirstSpotifyRepository(
        local = local,
        remote = remote,
        applicationScope = applicationScope
    )

    @Provides
    @ViewModelScoped
    fun provideGenreRemoteDataSource(
        remote: RemoteGenreDataSource,
        applicationScope: CoroutineScope,
    ): GenreRepository = OnlineGenreRepository(
        remote = remote,
        applicationScope = applicationScope
    )
}