package com.poulastaa.explore.network.di

import com.google.gson.Gson
import com.poulastaa.explore.domain.repository.album.RemoteExploreAlbumDatasource
import com.poulastaa.explore.domain.repository.all_from_artist.RemoteAllFromArtistDatasource
import com.poulastaa.explore.network.repository.all_from_artist.AllFromArtistAlbumPagingSource
import com.poulastaa.explore.network.repository.all_from_artist.AllFromArtistSongPagingSource
import com.poulastaa.explore.network.repository.all_from_artist.OkHttpAllFromArtistDatasource
import com.poulastaa.explore.network.repository.search.ExploreAlbumPagingSource
import com.poulastaa.explore.network.repository.search.OkHttpExploreAlbumDatasource
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

    @Provides
    @ViewModelScoped
    fun provideExploreAlbumPagingSource(
        client: OkHttpClient,
        gson: Gson,
    ): ExploreAlbumPagingSource = ExploreAlbumPagingSource(client, gson)

    @Provides
    @ViewModelScoped
    fun provideRemoteExploreAlbumDatasource(
        client: OkHttpClient,
        gson: Gson,
        album: ExploreAlbumPagingSource,
    ): RemoteExploreAlbumDatasource = OkHttpExploreAlbumDatasource(client, gson, album)
}