package com.poulastaa.kyoku.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.poulastaa.auth.network.repository.AuthHeaderInterceptor
import com.poulastaa.core.data.repository.DataStoreRepositoryImpl
import com.poulastaa.core.domain.DatastoreRepository
import com.poulastaa.kyoku.Kyoku
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatastorePreferences(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile(name = "KyokuAppPreferences")
        }
    )

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        dataStore: DataStore<Preferences>,
    ): DatastoreRepository = DataStoreRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager = CookieManager()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideHttClient(
        cookieManager: CookieManager,
        ds: DatastoreRepository,
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(AuthHeaderInterceptor(ds))
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()

    @Provides
    @Singleton
    fun provideCoroutineScope(
        application: Application,
    ): CoroutineScope = (application as Kyoku).applicationScope()
}