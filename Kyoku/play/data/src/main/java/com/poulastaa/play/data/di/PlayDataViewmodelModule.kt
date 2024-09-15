package com.poulastaa.play.data.di

import android.content.Context
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.add_playlist.AddPlaylistRepository
import com.poulastaa.core.domain.repository.add_playlist.LocalAddPlaylistDatasource
import com.poulastaa.core.domain.repository.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.AddToPlaylistRepository
import com.poulastaa.core.domain.repository.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.create_playlist.CreatePlaylistRepository
import com.poulastaa.core.domain.repository.create_playlist.LocalCreatePlaylistDatasource
import com.poulastaa.core.domain.repository.create_playlist.RemoteCreatePlaylistDatasource
import com.poulastaa.core.domain.repository.create_playlist.album.CreatePlaylistAlbumRepository
import com.poulastaa.core.domain.repository.create_playlist.album.RemoteCreatePlaylistAlbumDatasource
import com.poulastaa.core.domain.repository.create_playlist.artist.CreatePlaylistArtistRepository
import com.poulastaa.core.domain.repository.create_playlist.artist.LocalCreatePlaylistArtistDatasource
import com.poulastaa.core.domain.repository.create_playlist.artist.RemoteCreatePlaylistArtistDatasource
import com.poulastaa.core.domain.repository.explore_artist.ExploreArtistRepository
import com.poulastaa.core.domain.repository.explore_artist.LocalExploreArtistDatasource
import com.poulastaa.core.domain.repository.explore_artist.RemoteExploreArtistDatasource
import com.poulastaa.core.domain.repository.home.HomeRepository
import com.poulastaa.core.domain.repository.home.LocalHomeDatasource
import com.poulastaa.core.domain.repository.home.RemoteHomeDatasource
import com.poulastaa.core.domain.repository.library.LibraryRepository
import com.poulastaa.core.domain.repository.library.LocalLibraryDataSource
import com.poulastaa.core.domain.repository.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.repository.new_album.LocalNewAlbumDataSource
import com.poulastaa.core.domain.repository.new_album.NewAlbumRepository
import com.poulastaa.core.domain.repository.new_album.RemoteNewAlbumDataSource
import com.poulastaa.core.domain.repository.new_artist.LocalNewArtistDataSource
import com.poulastaa.core.domain.repository.new_artist.NewArtistRepository
import com.poulastaa.core.domain.repository.new_artist.RemoteNewArtistDataSource
import com.poulastaa.core.domain.repository.player.LocalPlayerDatasource
import com.poulastaa.core.domain.repository.player.PlayerRepository
import com.poulastaa.core.domain.repository.player.RemotePlayerDatasource
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
import com.poulastaa.play.data.OnlineFirstCreatePlaylistAlbumRepository
import com.poulastaa.play.data.OnlineFirstCreatePlaylistArtistRepository
import com.poulastaa.play.data.OnlineFirstCreatePlaylistRepository
import com.poulastaa.play.data.OnlineFirstExploreArtistRepository
import com.poulastaa.play.data.OnlineFirstHomeRepository
import com.poulastaa.play.data.OnlineFirstLibraryRepository
import com.poulastaa.play.data.OnlineFirstNewAlbumRepository
import com.poulastaa.play.data.OnlineFirstNewArtistRepository
import com.poulastaa.play.data.OnlineFirstPlayerRepository
import com.poulastaa.play.data.OnlineFirstViewArtistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @ViewModelScoped
    fun provideNewAlbumRepository(
        local: LocalNewAlbumDataSource,
        remote: RemoteNewAlbumDataSource,
        applicationScope: CoroutineScope,
    ): NewAlbumRepository = OnlineFirstNewAlbumRepository(
        local = local,
        remote = remote,
        applicationScope = applicationScope
    )

    @Provides
    @ViewModelScoped
    fun provideNewArtistRepository(
        local: LocalNewArtistDataSource,
        remote: RemoteNewArtistDataSource,
        applicationScope: CoroutineScope,
    ): NewArtistRepository = OnlineFirstNewArtistRepository(
        local = local,
        remote = remote,
        applicationScope = applicationScope,
    )

    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistRepository(
        local: LocalCreatePlaylistDatasource,
        remote: RemoteCreatePlaylistDatasource,
        applicationScope: CoroutineScope,
    ): CreatePlaylistRepository = OnlineFirstCreatePlaylistRepository(
        local = local,
        remote = remote,
        applicationScope = applicationScope,
    )

    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistArtistRepository(
        local: LocalCreatePlaylistArtistDatasource,
        remote: RemoteCreatePlaylistArtistDatasource,
    ): CreatePlaylistArtistRepository = OnlineFirstCreatePlaylistArtistRepository(
        local = local,
        remote = remote,
    )

    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistAlbumRepository(
        remote: RemoteCreatePlaylistAlbumDatasource,
    ): CreatePlaylistAlbumRepository = OnlineFirstCreatePlaylistAlbumRepository(remote = remote)

    @Provides
    @ViewModelScoped
    fun providePlayerRepository(
        @ApplicationContext context: Context,
        local: LocalPlayerDatasource,
        remote: RemotePlayerDatasource,
        applicationScope: CoroutineScope,
    ): PlayerRepository = OnlineFirstPlayerRepository(
        context = context,
        local = local,
        remote = remote,
        applicationScope = applicationScope
    )
}