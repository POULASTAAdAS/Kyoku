package com.poulastaa.network.di

import com.google.gson.Gson
import com.poulastaa.core.domain.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.home.RemoteHomeDatasource
import com.poulastaa.core.domain.library.RemoteLibraryDataSource
import com.poulastaa.network.OfflineFirstLibraryDatasource
import com.poulastaa.network.OnlineFirstAddPlaylistDatasource
import com.poulastaa.network.OnlineFirstAddToPlaylistDatasource
import com.poulastaa.network.OnlineFirstHomeDatasource
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
    fun provideHomeRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteHomeDatasource = OnlineFirstHomeDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideAddPlaylistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteAddPlaylistDatasource = OnlineFirstAddPlaylistDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideAddToPlaylistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteAddToPlaylistDatasource = OnlineFirstAddToPlaylistDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideLibraryRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteLibraryDataSource = OfflineFirstLibraryDatasource(
        client = client,
        gson = gson
    )
}