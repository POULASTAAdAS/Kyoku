package com.poulastaa.explore.network.di

import com.google.gson.Gson
import com.poulastaa.explore.domain.repository.RemoteAllFromArtistDatasource
import com.poulastaa.explore.network.repository.AllFromArtistAlbumPagingSource
import com.poulastaa.explore.network.repository.AllFromArtistSongPagingSource
import com.poulastaa.explore.network.repository.OkHttpAllFromArtistDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
internal object ExploreViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideAllFromArtistSongPagingSource(
        client: OkHttpClient,
        gson: Gson,
    ): AllFromArtistSongPagingSource = AllFromArtistSongPagingSource(client, gson)

    @Provides
    @ViewModelScoped
    fun provideAllFromArtistAlbumPagingSource(
        client: OkHttpClient,
        gson: Gson,
    ): AllFromArtistAlbumPagingSource = AllFromArtistAlbumPagingSource(client, gson)

    @Provides
    @ViewModelScoped
    fun provideAllFromArtistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
        song: AllFromArtistSongPagingSource,
        album: AllFromArtistAlbumPagingSource,
    ): RemoteAllFromArtistDatasource = OkHttpAllFromArtistDatasource(client, gson, song, album)
}