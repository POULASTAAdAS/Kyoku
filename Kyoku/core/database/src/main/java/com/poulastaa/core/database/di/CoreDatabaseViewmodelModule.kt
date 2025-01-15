package com.poulastaa.core.database.di

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.repository.RoomLocalImportPlaylistDatasource
import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
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
    fun provideImportPlaylistDatasource(
        dao: RootDao,
    ): LocalImportPlaylistDatasource = RoomLocalImportPlaylistDatasource(dao)
}