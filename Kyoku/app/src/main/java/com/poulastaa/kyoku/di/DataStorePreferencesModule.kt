package com.poulastaa.kyoku.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.poulastaa.kyoku.data.repository.DataStoreOperationImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStorePreferencesModule {
    @Provides
    @Singleton
    fun provideDataStorePreferences(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile(PREFERENCES_NAME)
        }
    )

    @Provides
    @Singleton
    fun provideDataStoreOperations(
        dataStore: DataStore<Preferences>
    ): DataStoreOperation = DataStoreOperationImpl(dataStore = dataStore)
}