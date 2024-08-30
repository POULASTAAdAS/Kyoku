package com.poulastaa.play.data.di

import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.add_playlist.AddPlaylistRepository
import com.poulastaa.core.domain.repository.add_playlist.LocalAddPlaylistDatasource
import com.poulastaa.core.domain.repository.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.AddToPlaylistRepository
import com.poulastaa.core.domain.repository.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.explore_artist.ExploreArtistRepository
import com.poulastaa.core.domain.repository.explore_artist.LocalExploreArtistDatasource
import com.poulastaa.core.domain.repository.explore_artist.RemoteExploreArtistDatasource
import com.poulastaa.core.domain.repository.home.HomeRepository
import com.poulastaa.core.domain.repository.home.LocalHomeDatasource
import com.poulastaa.core.domain.repository.home.RemoteHomeDatasource
import com.poulastaa.core.domain.repository.library.LibraryRepository
import com.poulastaa.core.domain.repository.library.LocalLibraryDataSource
import com.poulastaa.core.domain.repository.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.repository.setting.LocalSettingDatasource
import com.poulastaa.core.domain.repository.setting.SettingRepository
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
import com.poulastaa.core.domain.repository.view.RemoteViewDatasource
import com.poulastaa.core.domain.repository.view.ViewRepository
import com.poulastaa.core.domain.repository.view_artist.LocalViewArtistDatasource
import com.poulastaa.core.domain.repository.view_artist.RemoteViewArtistDatasource
import com.poulastaa.core.domain.repository.view_artist.ViewArtistRepository
import com.poulastaa.play.data.OfflineFirstSettingRepository
import com.poulastaa.play.data.OfflineFirstViewRepository
import com.poulastaa.play.data.OnlineFirstAddPlaylistRepository
import com.poulastaa.play.data.OnlineFirstAddToPlaylistRepository
import com.poulastaa.play.data.OnlineFirstExploreArtistRepository
import com.poulastaa.play.data.OnlineFirstHomeRepository
import com.poulastaa.play.data.OnlineFirstLibraryRepository
import com.poulastaa.play.data.OnlineFirstViewArtistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object PlayDataViewmodelModule {
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

    @Provides
    @ViewModelScoped
    fun provideViewRepository(
        local: LocalViewDatasource,
        remote: RemoteViewDatasource,
        applicationScope: CoroutineScope,
    ): ViewRepository = OfflineFirstViewRepository(
        local = local,
        remote = remote,
        application = applicationScope
    )

    @Provides
    @ViewModelScoped
    fun provideViewArtistRepository(
        local: LocalViewArtistDatasource,
        remote: RemoteViewArtistDatasource,
        applicationScope: CoroutineScope,
    ): ViewArtistRepository = OnlineFirstViewArtistRepository(
        local = local,
        remote = remote,
        application = applicationScope
    )


    @Provides
    @ViewModelScoped
    fun provideExploreArtistRepository(
        local: LocalExploreArtistDatasource,
        remote: RemoteExploreArtistDatasource,
        applicationScope: CoroutineScope,
    ): ExploreArtistRepository = OnlineFirstExploreArtistRepository(
        local = local,
        remote = remote,
        application = applicationScope
    )
}