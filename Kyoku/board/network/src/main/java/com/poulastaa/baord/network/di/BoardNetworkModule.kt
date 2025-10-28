package com.poulastaa.baord.network.di

import com.poulastaa.baord.network.repository.OkHttpImportPlaylistDatasource
import com.poulastaa.board.domain.import_playlist.ImportPlaylistRemoteDatasource
import com.poulastaa.core.network.domain.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object BoardNetworkModule {
    @Provides
    @ViewModelScoped
    fun provideRemoteImportPlaylistDatasource(
        api: ApiRepository,
    ): ImportPlaylistRemoteDatasource = OkHttpImportPlaylistDatasource(api)
}