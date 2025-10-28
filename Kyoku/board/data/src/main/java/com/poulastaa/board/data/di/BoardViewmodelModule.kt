package com.poulastaa.board.data.di

import com.poulastaa.board.data.repository.OfflineFirstImportPlaylistRepository
import com.poulastaa.board.data.repository.RoomImportPlaylistDatasource
import com.poulastaa.board.domain.import_playlist.ImportPlaylistLocalDatasource
import com.poulastaa.board.domain.import_playlist.ImportPlaylistRemoteDatasource
import com.poulastaa.board.domain.import_playlist.ImportPlaylistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object BoardViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideImportPlaylistRepository(
        local: ImportPlaylistLocalDatasource,
        remote: ImportPlaylistRemoteDatasource,
    ): ImportPlaylistRepository = OfflineFirstImportPlaylistRepository(local, remote)

    @Provides
    @ViewModelScoped
    fun provideImportPlaylistLocalDatasource(

    ): ImportPlaylistLocalDatasource = RoomImportPlaylistDatasource()
}