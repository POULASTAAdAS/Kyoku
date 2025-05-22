package com.poulastaa.core.database.di

import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.ExploreDao
import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.dao.ImportPlaylistDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.ProfileDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.repository.DatastoreLocalBDateDatasource
import com.poulastaa.core.database.repository.RoomAddArtistDatasource
import com.poulastaa.core.database.repository.RoomCommonInsertDatasource
import com.poulastaa.core.database.repository.RoomLocalAddAlbumDatasource
import com.poulastaa.core.database.repository.RoomLocalAddSongToPlaylistDatasource
import com.poulastaa.core.database.repository.RoomLocalAllFromArtistDatasource
import com.poulastaa.core.database.repository.RoomLocalCreatePlaylistDatasource
import com.poulastaa.core.database.repository.RoomLocalExploreAlbumDatasource
import com.poulastaa.core.database.repository.RoomLocalHomeDatasource
import com.poulastaa.core.database.repository.RoomLocalImportPlaylistDatasource
import com.poulastaa.core.database.repository.RoomLocalLibraryDatasource
import com.poulastaa.core.database.repository.RoomLocalProfileDatasource
import com.poulastaa.core.database.repository.RoomLocalSetArtistDatasource
import com.poulastaa.core.database.repository.RoomLocalSetGenreDatasource
import com.poulastaa.core.database.repository.RoomLocalSettingsDatasource
import com.poulastaa.core.database.repository.RoomLocalViewArtistDatasource
import com.poulastaa.core.database.repository.RoomLocalViewOtherDatasource
import com.poulastaa.core.database.repository.RoomLocalViewSavedItemDatasource
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalAddAlbumDatasource
import com.poulastaa.core.domain.repository.LocalAddArtistDatasource
import com.poulastaa.core.domain.repository.LocalAddSongToPlaylistDatasource
import com.poulastaa.core.domain.repository.LocalAllFromArtistDatasource
import com.poulastaa.core.domain.repository.LocalBDateDatasource
import com.poulastaa.core.domain.repository.LocalCreatePlaylistDatasource
import com.poulastaa.core.domain.repository.LocalExploreAlbumDatasource
import com.poulastaa.core.domain.repository.LocalHomeDatasource
import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
import com.poulastaa.core.domain.repository.LocalLibraryDatasource
import com.poulastaa.core.domain.repository.LocalProfileDatasource
import com.poulastaa.core.domain.repository.LocalSetArtistDatasource
import com.poulastaa.core.domain.repository.LocalSetGenreDatasource
import com.poulastaa.core.domain.repository.LocalSettingsDatasource
import com.poulastaa.core.domain.repository.LocalViewArtistDatasource
import com.poulastaa.core.domain.repository.LocalViewOtherDatasource
import com.poulastaa.core.domain.repository.LocalViewSavedItemDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
internal object CoreDatabaseViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideImportPlaylistDao(
        database: KyokuDatabase,
    ): ImportPlaylistDao = database.importPlaylistDao

    @Provides
    @ViewModelScoped
    fun provideImportPlaylistDatasource(
        dao: RootDao,
        local: ImportPlaylistDao,
    ): LocalImportPlaylistDatasource = RoomLocalImportPlaylistDatasource(dao, local)

    @Provides
    @ViewModelScoped
    fun provideLocalBDateDatasource(
        ds: DatastoreRepository,
    ): LocalBDateDatasource = DatastoreLocalBDateDatasource(ds)

    @Provides
    @ViewModelScoped
    fun provideLocalSetGenreDatasource(
        ds: DatastoreRepository,
        root: RootDao,
    ): LocalSetGenreDatasource = RoomLocalSetGenreDatasource(ds, root)

    @Provides
    @ViewModelScoped
    fun provideHomeDao(
        database: KyokuDatabase,
    ): HomeDao = database.homeDao

    @Provides
    @ViewModelScoped
    fun provideLocalSetArtistDatasource(
        ds: DatastoreRepository,
        root: RootDao,
    ): LocalSetArtistDatasource = RoomLocalSetArtistDatasource(root, ds)

    @Provides
    @ViewModelScoped
    fun provideLocalHomeDatasource(
        root: RootDao,
        home: HomeDao,
    ): LocalHomeDatasource = RoomLocalHomeDatasource(root, home)

    @Provides
    @ViewModelScoped
    fun provideLibraryDao(
        database: KyokuDatabase,
    ): LibraryDao = database.libraryDao

    @Provides
    @ViewModelScoped
    fun provideLocalLibraryDatasource(
        dao: LibraryDao,
    ): LocalLibraryDatasource = RoomLocalLibraryDatasource(dao)

    @Provides
    @ViewModelScoped
    fun provideLocalSettingsDatasource(
        database: KyokuDatabase,
    ): LocalSettingsDatasource = RoomLocalSettingsDatasource(database)

    @Provides
    @ViewModelScoped
    fun provideProfileDao(
        database: KyokuDatabase,
    ): ProfileDao = database.profileDao

    @Provides
    @ViewModelScoped
    fun provideLocalProfileDatasource(
        profileDao: ProfileDao,
    ): LocalProfileDatasource = RoomLocalProfileDatasource(profileDao)

    @Provides
    @ViewModelScoped
    fun provideViewDao(
        database: KyokuDatabase,
    ): ViewDao = database.viewDao

    @Provides
    @ViewModelScoped
    fun provideLocalViewArtistDatasource(
        viewDao: ViewDao,
    ): LocalViewArtistDatasource = RoomLocalViewArtistDatasource(viewDao)

    @Provides
    @ViewModelScoped
    fun provideLocalViewOtherDatasource(
        viewDao: ViewDao,
        root: RootDao,
        common: RoomCommonInsertDatasource,
    ): LocalViewOtherDatasource = RoomLocalViewOtherDatasource(viewDao, root, common)

    @Provides
    @ViewModelScoped
    fun provideLocalAllFromArtistDatasource(
        exploreDao: ExploreDao,
    ): LocalAllFromArtistDatasource = RoomLocalAllFromArtistDatasource(exploreDao)

    @Provides
    @ViewModelScoped
    fun provideLocalExploreAlbumDatasource(
        root: RootDao,
    ): LocalExploreAlbumDatasource = RoomLocalExploreAlbumDatasource(root)

    @Provides
    @ViewModelScoped
    fun provideLocalViewSavedItemDatasource(
        viewDao: ViewDao,
        scope: CoroutineScope,
    ): LocalViewSavedItemDatasource = RoomLocalViewSavedItemDatasource(viewDao, scope)

    @Provides
    @ViewModelScoped
    fun provideLocalAddSongToPlaylistDatasource(
        root: RootDao,
        view: ViewDao,
        common: RoomCommonInsertDatasource,
    ): LocalAddSongToPlaylistDatasource = RoomLocalAddSongToPlaylistDatasource(
        rootDao = root,
        viewDao = view,
        common = common
    )

    @Provides
    @ViewModelScoped
    fun provideLocalCreatePlaylistDatasource(
        root: RootDao,
    ): LocalCreatePlaylistDatasource = RoomLocalCreatePlaylistDatasource(root)

    @Provides
    @ViewModelScoped
    fun provideLocalAddAlbumDatasource(
        root: RootDao,
    ): LocalAddAlbumDatasource = RoomLocalAddAlbumDatasource(root)

    @Provides
    @ViewModelScoped
    fun provideAddArtistDatasource(
        root: RootDao,
        libraryDao: LibraryDao,
    ): LocalAddArtistDatasource = RoomAddArtistDatasource(root, libraryDao)
}