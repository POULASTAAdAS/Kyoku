package com.poulastaa.main.network.di

import com.google.gson.Gson
import com.poulastaa.main.domain.repository.work.RemoteRefreshDatasource
import com.poulastaa.main.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.main.network.repository.OkHttpRefreshDatasource
import com.poulastaa.main.network.repository.OkHttpWorkDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MainNetworkAppModule {
    @Provides
    @Singleton
    fun provideRemoteWorkDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteWorkDatasource = OkHttpWorkDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @Singleton
    fun provideRemoteRefreshDatasource(
        client: OkHttpClient,
        gson: Gson
    ): RemoteRefreshDatasource = OkHttpRefreshDatasource(client, gson)
}