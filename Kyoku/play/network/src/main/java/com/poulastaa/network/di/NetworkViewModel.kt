package com.poulastaa.network.di

import com.google.gson.Gson
import com.poulastaa.core.domain.repository.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.explore_artist.RemoteExploreArtistDatasource
import com.poulastaa.core.domain.repository.home.RemoteHomeDatasource
import com.poulastaa.core.domain.repository.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.repository.view.RemoteViewDatasource
import com.poulastaa.core.domain.repository.view_artist.RemoteViewArtistDatasource
import com.poulastaa.network.OfflineFirstLibraryDatasource
import com.poulastaa.network.OnlineFirstAddPlaylistDatasource
import com.poulastaa.network.OnlineFirstAddToPlaylistDatasource
import com.poulastaa.network.OnlineFirstExploreArtistDatasource
import com.poulastaa.network.OnlineFirstHomeDatasource
import com.poulastaa.network.OnlineFirstViewArtistDatasource
import com.poulastaa.network.OnlineFirstViewDatasource
import com.poulastaa.paging_source.ExploreArtistAlbumPagerSource
import com.poulastaa.paging_source.ExploreArtistSongPagerSource
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
}