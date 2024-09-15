package com.poulastaa.network.di

import com.google.gson.Gson
import com.poulastaa.core.domain.repository.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.create_playlist.RemoteCreatePlaylistDatasource
import com.poulastaa.core.domain.repository.create_playlist.album.RemoteCreatePlaylistAlbumDatasource
import com.poulastaa.core.domain.repository.create_playlist.artist.RemoteCreatePlaylistArtistDatasource
import com.poulastaa.core.domain.repository.explore_artist.RemoteExploreArtistDatasource
import com.poulastaa.core.domain.repository.home.RemoteHomeDatasource
import com.poulastaa.core.domain.repository.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.repository.new_album.RemoteNewAlbumDataSource
import com.poulastaa.core.domain.repository.new_artist.RemoteNewArtistDataSource
import com.poulastaa.core.domain.repository.player.RemotePlayerDatasource
import com.poulastaa.core.domain.repository.view.RemoteViewDatasource
import com.poulastaa.core.domain.repository.view_artist.RemoteViewArtistDatasource
import com.poulastaa.network.OfflineFirstLibraryDatasource
import com.poulastaa.network.OnlineFirstAddPlaylistDatasource
import com.poulastaa.network.OnlineFirstAddToPlaylistDatasource
import com.poulastaa.network.OnlineFirstCreatePlaylistAlbumDatasource
import com.poulastaa.network.OnlineFirstCreatePlaylistArtistDatasource
import com.poulastaa.network.OnlineFirstCreatePlaylistDatasource
import com.poulastaa.network.OnlineFirstExploreArtistDatasource
import com.poulastaa.network.OnlineFirstHomeDatasource
import com.poulastaa.network.OnlineFirstNewAlbumDatasource
import com.poulastaa.network.OnlineFirstNewArtistDatasource
import com.poulastaa.network.OnlineFirstPlayerDatasource
import com.poulastaa.network.OnlineFirstViewArtistDatasource
import com.poulastaa.network.OnlineFirstViewDatasource
import com.poulastaa.network.paging_source.CreatePlaylistPagerSource
import com.poulastaa.network.paging_source.ExploreArtistAlbumPagerSource
import com.poulastaa.network.paging_source.ExploreArtistSongPagerSource
import com.poulastaa.network.paging_source.NewAlbumPagerSource
import com.poulastaa.network.paging_source.NewArtistPagerSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
object NetworkViewModel {
    @Provides
    @ViewModelScoped
    fun provideHomeRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteHomeDatasource = OnlineFirstHomeDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideAddPlaylistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteAddPlaylistDatasource = OnlineFirstAddPlaylistDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideAddToPlaylistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteAddToPlaylistDatasource = OnlineFirstAddToPlaylistDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideLibraryRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteLibraryDataSource = OfflineFirstLibraryDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideViewRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteViewDatasource = OnlineFirstViewDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideViewArtistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteViewArtistDatasource = OnlineFirstViewArtistDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideExploreArtistAlbumPagerSource(
        client: OkHttpClient,
        gson: Gson,
    ): ExploreArtistAlbumPagerSource = ExploreArtistAlbumPagerSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideExploreArtistSongPagerSource(
        client: OkHttpClient,
        gson: Gson,
    ): ExploreArtistSongPagerSource = ExploreArtistSongPagerSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideExploreArtistRemoteDatasource(
        client: OkHttpClient,
        gson: Gson,
        pagerAlbum: ExploreArtistAlbumPagerSource,
        pagerSong: ExploreArtistSongPagerSource
    ): RemoteExploreArtistDatasource = OnlineFirstExploreArtistDatasource(
        client = client,
        gson = gson,
        pagerAlbum = pagerAlbum,
        pagerSong = pagerSong,
    )

    @Provides
    @ViewModelScoped
    fun provideNewAlbumPagerSource(
        client: OkHttpClient,
        gson: Gson,
    ): NewAlbumPagerSource = NewAlbumPagerSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideNewAlbumDatasource(
        client: OkHttpClient,
        gson: Gson,
        pager: NewAlbumPagerSource,
    ): RemoteNewAlbumDataSource = OnlineFirstNewAlbumDatasource(
        client = client,
        gson = gson,
        pager = pager,
    )

    @Provides
    @ViewModelScoped
    fun provideNewArtistPagerSource(
        client: OkHttpClient,
        gson: Gson,
    ): NewArtistPagerSource = NewArtistPagerSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideNewArtistDatasource(
        client: OkHttpClient,
        gson: Gson,
        pager: NewArtistPagerSource,
    ): RemoteNewArtistDataSource = OnlineFirstNewArtistDatasource(
        client = client,
        gson = gson,
        pager = pager,
    )

    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistPagerSource(
        client: OkHttpClient,
        gson: Gson,
    ): CreatePlaylistPagerSource = CreatePlaylistPagerSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistDatasource(
        client: OkHttpClient,
        gson: Gson,
        pager: CreatePlaylistPagerSource
    ): RemoteCreatePlaylistDatasource = OnlineFirstCreatePlaylistDatasource(
        client = client,
        gson = gson,
        pager = pager
    )

    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistArtistDatasource(
        client: OkHttpClient,
        gson: Gson,
        album: ExploreArtistAlbumPagerSource,
        song: ExploreArtistSongPagerSource
    ): RemoteCreatePlaylistArtistDatasource = OnlineFirstCreatePlaylistArtistDatasource(
        client = client,
        gson = gson,
        album = album,
        song = song
    )

    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistAlbumDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteCreatePlaylistAlbumDatasource = OnlineFirstCreatePlaylistAlbumDatasource(
        client = client,
        gson = gson,
    )

    @Provides
    @ViewModelScoped
    fun providePlayerDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemotePlayerDatasource = OnlineFirstPlayerDatasource(
        client = client,
        gson = gson,
    )
}