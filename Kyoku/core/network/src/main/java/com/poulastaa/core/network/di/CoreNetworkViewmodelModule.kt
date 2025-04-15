package com.poulastaa.core.network.di

import com.google.gson.Gson
import com.poulastaa.core.domain.repository.RemoteCreatePlaylistDatasource
import com.poulastaa.core.network.repository.OkHttpCratePlaylistDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
internal object CoreNetworkViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideRemoteCreatePlaylistDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteCreatePlaylistDatasource = OkHttpCratePlaylistDatasource(client, gson)
}