package com.poulastaa.setup.network.di

import com.google.gson.Gson
import com.poulastaa.setup.domain.repository.import_playlist.RemoteImportPlaylistDatasource
import com.poulastaa.setup.domain.repository.set_bdate.RemoteBDateDatasource
import com.poulastaa.setup.network.repository.OkHttpImportPlaylistDatasource
import com.poulastaa.setup.network.repository.OkHttpSetBDateDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
object SetupNetworkModule {
    @Provides
    @ViewModelScoped
    fun provideImportPlaylistDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteImportPlaylistDatasource = OkHttpImportPlaylistDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideBDateDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteBDateDatasource = OkHttpSetBDateDatasource(
        client = client,
        gson = gson
    )
}