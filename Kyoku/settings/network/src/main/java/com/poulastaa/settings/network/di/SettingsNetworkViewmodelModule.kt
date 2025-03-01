package com.poulastaa.settings.network.di

import com.google.gson.Gson
import com.poulastaa.settings.domain.repository.RemoteSettingDatasource
import com.poulastaa.settings.network.repository.OkHttpRemoteSettingsDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient


@Module
@InstallIn(ViewModelComponent::class)
object SettingsNetworkViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideRemoteSettingDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteSettingDatasource = OkHttpRemoteSettingsDatasource(client, gson)
}