package com.poulastaa.network.di

import com.google.gson.Gson
import com.poulastaa.core.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.network.OnlineFirstWorkDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkAppModule {
    @Provides
    @Singleton
    fun provideWorkRemoteSource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteWorkDatasource = OnlineFirstWorkDatasource(
        client = client,
        gson = gson
    )
}