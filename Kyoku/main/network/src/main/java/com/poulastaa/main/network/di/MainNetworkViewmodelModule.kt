package com.poulastaa.main.network.di

import com.google.gson.Gson
import com.poulastaa.main.domain.repository.RemoteHomeDataSource
import com.poulastaa.main.network.repository.OkHttpHomeDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
object MainNetworkViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideRemoteHomeDatasource(
        gson: Gson,
        client: OkHttpClient,
    ): RemoteHomeDataSource = OkHttpHomeDatasource(
        gson = gson,
        client = client
    )
}