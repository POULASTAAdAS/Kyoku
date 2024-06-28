package com.poulastaa.setup.data.di

import com.poulastaa.core.domain.get_spotify_playlist.LocalSpotifyDataSource
import com.poulastaa.core.domain.get_spotify_playlist.RemoteSpotifyDataSource
import com.poulastaa.core.domain.get_spotify_playlist.SpotifyRepository
import com.poulastaa.setup.data.OnlineFirstSpotifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object SpotifyModule {
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
}