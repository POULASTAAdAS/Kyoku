package com.poulastaa.auth.network.di

import com.google.gson.Gson
import com.poulastaa.auth.network.OnlineFirstAuthDatasource
import com.poulastaa.core.domain.auth.RemoteAuthDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {
    @Provides
    @ViewModelScoped
    @Named("AuthClient")
    fun provideOkHttpClient(
        cookieManager: CookieManager,
    ): OkHttpClient = OkHttpClient
        .Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()

    @Provides
    @ViewModelScoped
    fun provideAuthRemoteDataSource(
        @Named("AuthClient")
        client: OkHttpClient,
        gson: Gson,
    ): RemoteAuthDatasource = OnlineFirstAuthDatasource(
        client = client,
        gson = gson
    )
}