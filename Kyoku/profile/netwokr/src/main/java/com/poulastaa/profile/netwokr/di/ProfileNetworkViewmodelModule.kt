package com.poulastaa.profile.netwokr.di

import com.google.gson.Gson
import com.poulastaa.profile.domain.repository.RemoteProfileDatasource
import com.poulastaa.profile.netwokr.repository.OkHttpProfileDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
object ProfileNetworkViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideRemoteProfileDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteProfileDatasource = OkHttpProfileDatasource(client, gson)
}