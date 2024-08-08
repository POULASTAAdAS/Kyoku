package com.poulastaa.core.database.di

import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.AddPlaylistDao
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.GetSpotifyPlaylistDao
import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.repository.RoomLocalAddPlaylistDatasource
import com.poulastaa.core.database.repository.RoomLocalAuthDatasource
import com.poulastaa.core.database.repository.RoomLocalHomeDatasource
import com.poulastaa.core.database.repository.RoomLocalLibraryDataSource
import com.poulastaa.core.database.repository.RoomLocalSpotifyDataSource
import com.poulastaa.core.database.repository.RoomLocalStoreArtistDataSource
import com.poulastaa.core.domain.add_playlist.LocalAddPlaylistDatasource
import com.poulastaa.core.domain.artist.LocalArtistDataSource
import com.poulastaa.core.domain.auth.LocalAuthDatasource
import com.poulastaa.core.domain.get_spotify_playlist.LocalSpotifyDataSource
import com.poulastaa.core.domain.home.LocalHomeDatasource
import com.poulastaa.core.domain.library.LocalLibraryDataSource
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

    @Provides
    @ViewModelScoped
    fun provideAuthDatasource(
        commonDao: CommonDao,
        homeDao: HomeDao,
    ): LocalAuthDatasource = RoomLocalAuthDatasource(
        commonDao = commonDao,
        homeDao = homeDao,
    )

    @Provides
    @ViewModelScoped
    fun provideLocalSpotifyDataSource(
        commonDao: CommonDao,
        getSpotifyPlaylistDao: GetSpotifyPlaylistDao,
    ): LocalSpotifyDataSource = RoomLocalSpotifyDataSource(
        commonDao = commonDao,
        spotifyDao = getSpotifyPlaylistDao
    )

    @Provides
    @ViewModelScoped
    fun provideLocalStoreArtistDataSource(
        commonDao: CommonDao,
    ): LocalArtistDataSource = RoomLocalStoreArtistDataSource(
        commonDao = commonDao
    )

    @Provides
    @ViewModelScoped
    fun provideHomeDao(
        database: KyokuDatabase,
    ): HomeDao = database.homeDao

    @Provides
    @ViewModelScoped
    fun provideLocalHomeDatasource(
        homeDao: HomeDao,
        commonDao: CommonDao,
    ): LocalHomeDatasource = RoomLocalHomeDatasource(
        commonDao = commonDao,
        homeDao = homeDao
    )

    @Provides
    @ViewModelScoped
    fun provideLibraryDao(
        database: KyokuDatabase,
    ): LibraryDao = database.libraryDao

    @Provides
    @ViewModelScoped
    fun provideAddPlaylistDao(
        database: KyokuDatabase,
    ): AddPlaylistDao = database.addPlaylistDao

    @Provides
    @ViewModelScoped
    fun provideLocalLibraryDatasource(
        libraryDao: LibraryDao,
        commonDao: CommonDao,
    ): LocalLibraryDataSource = RoomLocalLibraryDataSource(
        libraryDao = libraryDao,
        commonDao = commonDao
    )

    @Provides
    @ViewModelScoped
    fun provideLocalAddPlaylistDatasource(
        addPlaylistDao: AddPlaylistDao,
        commonDao: CommonDao,
    ): LocalAddPlaylistDatasource = RoomLocalAddPlaylistDatasource(
        commonDao = commonDao,
        addPlaylistDao = addPlaylistDao
    )
}