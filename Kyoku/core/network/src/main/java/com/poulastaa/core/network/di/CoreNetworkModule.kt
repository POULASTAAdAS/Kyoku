package com.poulastaa.core.network.di

import com.google.gson.Gson
import com.poulastaa.core.network.data.OkHttpDatasource
import com.poulastaa.core.network.domain.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object CoreNetworkModule {
    @Provides
    @ViewModelScoped
    fun provideCoreApi(
        @Named("authClient")
        authClient: OkHttpClient,
        @Named("mainClient")
        mainClient: OkHttpClient,
        gson: Gson
    ): ApiRepository = OkHttpDatasource(authClient,mainClient, gson)
}