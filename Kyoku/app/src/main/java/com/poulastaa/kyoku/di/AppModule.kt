package com.poulastaa.kyoku.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.poulastaa.core.data.ConfigProviderRepositoryImpl
import com.poulastaa.core.data.DataStoreRepositoryImpl
import com.poulastaa.core.domain.ConfigProviderRepository
import com.poulastaa.core.domain.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
            context.preferencesDataStoreFile("AppPreferences")
        }
    )

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        dataStore: DataStore<Preferences>,
    ): DataStoreRepository = DataStoreRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideConfig(): ConfigProviderRepository = ConfigProviderRepositoryImpl()

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager = CookieManager()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}