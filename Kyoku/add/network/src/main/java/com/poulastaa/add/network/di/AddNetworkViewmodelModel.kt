package com.poulastaa.add.network.di

import com.google.gson.Gson
import com.poulastaa.add.domain.repository.RemoteAddAlbumDatasource
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.add.network.repository.AddAlbumPagingSource
import com.poulastaa.add.network.repository.AddSongToPlaylistArtistPagingSource
import com.poulastaa.add.network.repository.AddSongToPlaylistPagingSource
import com.poulastaa.add.network.repository.OkHttpAddAlbumDatasource
import com.poulastaa.add.network.repository.OkHttpAddSongToPlaylistDatasource
import com.poulastaa.view.domain.repository.RemoteViewOtherDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
internal object AddNetworkViewmodelModel {
    @Provides
    @ViewModelScoped
    fun provideAddSongToPlaylistPagingSource(
        client: OkHttpClient,
        gson: Gson,
    ): AddSongToPlaylistPagingSource = AddSongToPlaylistPagingSource(client, gson)

    @Provides
    @ViewModelScoped
    fun provideAddSongToPlaylistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
        playlistPager: AddSongToPlaylistPagingSource,
        artistPager: AddSongToPlaylistArtistPagingSource,
        view: RemoteViewOtherDatasource,
    ): RemoteAddSongToPlaylistDatasource = OkHttpAddSongToPlaylistDatasource(
        client = client,
        gson = gson,
        playlistPager = playlistPager,
        artistPager = artistPager,
        view = view
    )

    @Provides
    @ViewModelScoped
    fun provideAddAlbumPagingSource(
        client: OkHttpClient,
        gson: Gson,
    ): AddAlbumPagingSource = AddAlbumPagingSource(client, gson)

    @Provides
    @ViewModelScoped
    fun provideAddAlbumRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
        album: AddAlbumPagingSource,
    ): RemoteAddAlbumDatasource = OkHttpAddAlbumDatasource(
        client = client,
        gson = gson,
        album = album
    )
}