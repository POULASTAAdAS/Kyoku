package com.poulastaa.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import coil3.intercept.Interceptor
import com.google.gson.Gson
import com.poulastaa.core.data.repsitory.KyokuImageLoader
import com.poulastaa.core.data.repsitory.PreferencesDatStoreRepositoryImpl
import com.poulastaa.core.domain.repository.PreferencesDatastoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreAppModule {
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
    fun providePreferencesDatastoreRepository(
        ds: DataStore<Preferences>,
        gson: Gson,
    ): PreferencesDatastoreRepository = PreferencesDatStoreRepositoryImpl(ds, gson)

    @Provides
    @Singleton
    fun provideKyokuImageLoader(
        @Named("ImageAuthHeaderInterceptor")
        interceptor: Interceptor,
    ) = KyokuImageLoader(interceptor)
}