package com.poulastaa.auth.data.di

import com.google.gson.Gson
import com.poulastaa.auth.data.AuthRepositoryImpl
import com.poulastaa.auth.data.UserDataValidator
import com.poulastaa.auth.domain.Validator
import com.poulastaa.auth.domain.auth.AuthRepository
import com.poulastaa.core.domain.DataStoreRepository
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
object AuthModule {
    @Provides
    @ViewModelScoped
    fun provideValidator(): Validator = UserDataValidator()

    @Provides
    @ViewModelScoped
    @Named("AuthHttpClient")
    fun provideOkHttpClient(cookieManager: CookieManager): OkHttpClient = OkHttpClient
        .Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(
        gson: Gson,
        @Named("AuthHttpClient") client: OkHttpClient,
        ds: DataStoreRepository,
        cookieManager: CookieManager,
    ): AuthRepository = AuthRepositoryImpl(
        gson = gson,
        client = client,
        ds = ds,
        cookieManager = cookieManager
    )
}