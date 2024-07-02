package com.poulastaa.setup.network.di

import com.google.gson.Gson
import com.poulastaa.core.domain.b_date.RemoteBDateDataSource
import com.poulastaa.core.domain.genre.RemoteGenreDataSource
import com.poulastaa.core.domain.get_spotify_playlist.RemoteSpotifyDataSource
import com.poulastaa.setup.network.BDateRemoteDataSource
import com.poulastaa.setup.network.GenreRemoteDataSource
import com.poulastaa.setup.network.SpotifyRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
object NetworkViewModel {
    @Provides
    @ViewModelScoped
    fun provideSpotifyRemoteDataSource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteSpotifyDataSource = SpotifyRemoteDataSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideBDateRemoteDataSource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteBDateDataSource = BDateRemoteDataSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideGenreRemoteDataSource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteGenreDataSource = GenreRemoteDataSource(
        client = client,
        gson = gson
    )
}