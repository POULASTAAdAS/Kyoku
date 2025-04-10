package com.poulastaa.add.network.di

import com.google.gson.Gson
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.add.network.repository.AddSongToPlaylistPagingSource
import com.poulastaa.add.network.repository.OkHttpAddSongToPlaylistDatasource
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
        pager: AddSongToPlaylistPagingSource,
    ): RemoteAddSongToPlaylistDatasource = OkHttpAddSongToPlaylistDatasource(client, gson, pager)
}