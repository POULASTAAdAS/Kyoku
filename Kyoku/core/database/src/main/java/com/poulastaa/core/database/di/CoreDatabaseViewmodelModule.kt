package com.poulastaa.core.database.di

import com.poulastaa.core.database.KyokuDatabase
import com.poulastaa.core.database.dao.ImportPlaylistDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.repository.DatastoreLocalBDateDatasource
import com.poulastaa.core.database.repository.RoomLocalImportPlaylistDatasource
import com.poulastaa.core.database.repository.RoomLocalSetArtistDatasource
import com.poulastaa.core.database.repository.RoomLocalSetGenreDatasource
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalBDateDatasource
import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
import com.poulastaa.core.domain.repository.LocalSetArtistDatasource
import com.poulastaa.core.domain.repository.LocalSetGenreDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CoreDatabaseViewmodelModule {
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
    fun provideLocalSetArtistDatasource(
        ds: DatastoreRepository,
        root: RootDao,
    ): LocalSetArtistDatasource = RoomLocalSetArtistDatasource(root, ds)
}