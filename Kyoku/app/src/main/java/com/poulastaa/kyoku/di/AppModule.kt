package com.poulastaa.kyoku.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.poulastaa.core.presentation.ThemeManager
import com.poulastaa.kyoku.Kyoku
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.java.net.cookiejar.JavaNetCookieJar
import java.net.CookieHandler
import java.net.CookieManager
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCoroutineScope(application: Application) = (application as Kyoku).applicationScope()

    @Provides
    @Singleton
    fun provideThemeManager(): ThemeManager {
        val themeManager = ThemeManager()
        ThemeManager.setInstance(themeManager)
        return themeManager
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    @Named("authClient")
    fun provideAuthHttClient(): OkHttpClient = OkHttpClient
        .Builder()
        .build()

    @Provides
    @Singleton
    fun provideCookieHandler(): CookieHandler = CookieHandler.getDefault() ?: CookieManager()

    @Provides
    @Singleton
    @Named("mainClient")
    fun provideMainHttClient(
        cookieManager: CookieHandler,
        gson: Gson,
        @ApplicationContext context: Context,
    ): OkHttpClient = OkHttpClient
        .Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()
}