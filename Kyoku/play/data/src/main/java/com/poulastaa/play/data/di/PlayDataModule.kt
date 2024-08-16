package com.poulastaa.play.data.di

import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.add_playlist.AddPlaylistRepository
import com.poulastaa.core.domain.add_playlist.LocalAddPlaylistDatasource
import com.poulastaa.core.domain.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.add_to_playlist.AddToPlaylistRepository
import com.poulastaa.core.domain.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.home.HomeRepository
import com.poulastaa.core.domain.home.LocalHomeDatasource
import com.poulastaa.core.domain.home.RemoteHomeDatasource
import com.poulastaa.core.domain.library.LibraryRepository
import com.poulastaa.core.domain.library.LocalLibraryDataSource
import com.poulastaa.core.domain.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.setting.LocalSettingDatasource
import com.poulastaa.core.domain.setting.SettingRepository
import com.poulastaa.play.data.OfflineFirstSettingRepository
import com.poulastaa.play.data.OnlineFirstAddPlaylistRepository
import com.poulastaa.play.data.OnlineFirstAddToPlaylistRepository
import com.poulastaa.play.data.OnlineFirstHomeRepository
import com.poulastaa.play.data.OnlineFirstLibraryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object PlayDataModule {
    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        local: LocalHomeDatasource,
        remote: RemoteHomeDatasource,
        applicationScope: CoroutineScope,
    ): HomeRepository = OnlineFirstHomeRepository(
        local = local,
        remote = remote,
        application = applicationScope
    )

    @Provides
    @ViewModelScoped
    fun provideLibraryRepository(
        local: LocalLibraryDataSource,
        remote: RemoteLibraryDataSource,
        applicationScope: CoroutineScope,
        ds: DataStoreRepository
    ): LibraryRepository = OnlineFirstLibraryRepository(
        local = local,
        remote = remote,
        application = applicationScope,
        ds = ds
    )

    @Provides
    @ViewModelScoped
    fun provideAddPlaylistRepository(
        local: LocalAddPlaylistDatasource,
        remote: RemoteAddPlaylistDatasource,
        applicationScope: CoroutineScope,
    ): AddPlaylistRepository = OnlineFirstAddPlaylistRepository(
        local = local,
        remote = remote,
        application = applicationScope
    )

    @Provides
    @ViewModelScoped
    fun provideSettingRepository(
        local: LocalSettingDatasource,
        ds: DataStoreRepository,
        applicationScope: CoroutineScope,
    ): SettingRepository = OfflineFirstSettingRepository(
        ds = ds,
        local = local,
        application = applicationScope
    )

    @Provides
    @ViewModelScoped
    fun provideAddToPlaylistRepository(
        local: LocalAddToPlaylistDatasource,
        remote: RemoteAddToPlaylistDatasource,
        applicationScope: CoroutineScope,
    ): AddToPlaylistRepository = OnlineFirstAddToPlaylistRepository(
        local = local,
        remote = remote,
        application = applicationScope
    )
}