package com.poulastaa.network.di

import com.google.gson.Gson
import com.poulastaa.core.domain.home.RemoteHomeDatasource
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
}