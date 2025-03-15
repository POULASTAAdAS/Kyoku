package com.poulastaa.view.network.di

import com.google.gson.Gson
import com.poulastaa.view.domain.repository.RemoteViewArtistDatasource
import com.poulastaa.view.domain.repository.RemoteViewOtherDatasource
import com.poulastaa.view.network.repository.OkHttpViewArtistDatasource
import com.poulastaa.view.network.repository.OkHttpViewOtherDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
internal object ViewNetworkViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideRemoteViewArtistDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteViewArtistDatasource = OkHttpViewArtistDatasource(client, gson)

    @Provides
    @ViewModelScoped
    fun provideRemoteViewOtherDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteViewOtherDatasource = OkHttpViewOtherDatasource(client, gson)
}