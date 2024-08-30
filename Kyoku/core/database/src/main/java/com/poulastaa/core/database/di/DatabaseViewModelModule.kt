package com.poulastaa.core.database.di

import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.AddPlaylistDao
import com.poulastaa.core.database.dao.AddToPlaylistDao
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.GetSpotifyPlaylistDao
import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.SettingDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.dao.WorkDao
import com.poulastaa.core.database.repository.RoomLocalAddPlaylistDatasource
import com.poulastaa.core.database.repository.RoomLocalAddToPlaylistDatasource
import com.poulastaa.core.database.repository.RoomLocalAuthDatasource
import com.poulastaa.core.database.repository.RoomLocalExploreArtistDatasource
import com.poulastaa.core.database.repository.RoomLocalHomeDatasource
import com.poulastaa.core.database.repository.RoomLocalLibraryDataSource
import com.poulastaa.core.database.repository.RoomLocalSettingDatasource
import com.poulastaa.core.database.repository.RoomLocalSpotifyDataSource
import com.poulastaa.core.database.repository.RoomLocalStoreArtistDataSource
import com.poulastaa.core.database.repository.RoomLocalViewArtistDatasource
import com.poulastaa.core.database.repository.RoomLocalViewDatasource
import com.poulastaa.core.database.repository.RoomLocalWorkDatasource
import com.poulastaa.core.domain.repository.add_playlist.LocalAddPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.artist.LocalArtistDataSource
import com.poulastaa.core.domain.repository.auth.LocalAuthDatasource
import com.poulastaa.core.domain.repository.explore_artist.LocalExploreArtistDatasource
import com.poulastaa.core.domain.repository.get_spotify_playlist.LocalSpotifyDataSource
import com.poulastaa.core.domain.repository.home.LocalHomeDatasource
import com.poulastaa.core.domain.repository.library.LocalLibraryDataSource
import com.poulastaa.core.domain.repository.setting.LocalSettingDatasource
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
import com.poulastaa.core.domain.repository.view_artist.LocalViewArtistDatasource
import com.poulastaa.core.domain.repository.work.LocalWorkDatasource
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

    @Provides
    @ViewModelScoped
    fun provideSettingDao(
        database: KyokuDatabase,
    ): SettingDao = database.settingDao

    @Provides
    @ViewModelScoped
    fun provideLocalSettingDatasource(
        settingDao: SettingDao,
        database: KyokuDatabase,
    ): LocalSettingDatasource = RoomLocalSettingDatasource(
        database = database,
        settingDao = settingDao
    )

    @Provides
    @ViewModelScoped
    fun provideAddToPlaylistDao(
        database: KyokuDatabase,
    ): AddToPlaylistDao = database.addToPlaylist

    @Provides
    @ViewModelScoped
    fun provideLocalAddToPlaylistDatasource(
        commonDao: CommonDao,
        addToPlaylistDao: AddToPlaylistDao,
    ): LocalAddToPlaylistDatasource = RoomLocalAddToPlaylistDatasource(
        commonDao = commonDao,
        addToPlaylistDao = addToPlaylistDao
    )

    @Provides
    @ViewModelScoped
    fun provideViewDao(
        database: KyokuDatabase,
    ): ViewDao = database.viewDao

    @Provides
    @ViewModelScoped
    fun provideLocalViewDatasource(
        commonDao: CommonDao,
        viewDao: ViewDao
    ): LocalViewDatasource = RoomLocalViewDatasource(
        commonDao = commonDao,
        viewDao = viewDao
    )

    @Provides
    @ViewModelScoped
    fun provideLocalViewArtistDatasource(
        commonDao: CommonDao,
    ): LocalViewArtistDatasource = RoomLocalViewArtistDatasource(commonDao = commonDao)

    @Provides
    @ViewModelScoped
    fun provideLocalExploreArtistDatasource(
        commonDao: CommonDao,
        viewDao: ViewDao
    ): LocalExploreArtistDatasource = RoomLocalExploreArtistDatasource(
        commonDao = commonDao,
        viewDao = viewDao
    )

    @Provides
    @ViewModelScoped
    fun provideWorkDao(
        database: KyokuDatabase,
    ): WorkDao = database.workDao

    @Provides
    @ViewModelScoped
    fun provideLocalWorkDatasource(
        commonDao: CommonDao,
        workDao: WorkDao
    ): LocalWorkDatasource = RoomLocalWorkDatasource(
        commonDao = commonDao,
        workDao = workDao
    )
}