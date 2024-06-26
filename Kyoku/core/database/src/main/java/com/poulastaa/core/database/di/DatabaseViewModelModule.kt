package com.poulastaa.core.database.di

import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.GetSpotifyPlaylistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideGetSpotifyPlaylistDao(
        database: KyokuDatabase,
    ): GetSpotifyPlaylistDao = database.getSpotifyPlaylistDao
}