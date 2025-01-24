package com.poulastaa.setup.network.di

import com.google.gson.Gson
import com.poulastaa.setup.domain.repository.import_playlist.RemoteImportPlaylistDatasource
import com.poulastaa.setup.domain.repository.set_artist.RemoteSetArtistDatasource
import com.poulastaa.setup.domain.repository.set_bdate.RemoteBDateDatasource
import com.poulastaa.setup.domain.repository.set_genre.RemoteSetGenreDatasource
import com.poulastaa.setup.network.paging_source.SuggestArtistPagingSource
import com.poulastaa.setup.network.paging_source.SuggestGenrePagingSource
import com.poulastaa.setup.network.repository.OkHttpImportPlaylistDatasource
import com.poulastaa.setup.network.repository.OkHttpSetArtistDatasource
import com.poulastaa.setup.network.repository.OkHttpSetBDateDatasource
import com.poulastaa.setup.network.repository.OkHttpSetGenreDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ViewModelComponent::class)
object SetupNetworkModule {
    @Provides
    @ViewModelScoped
    fun provideImportPlaylistDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteImportPlaylistDatasource = OkHttpImportPlaylistDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideBDateDatasource(
        client: OkHttpClient,
        gson: Gson,
    ): RemoteBDateDatasource = OkHttpSetBDateDatasource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideSuggestGenreRemotePageSource(
        client: OkHttpClient,
        gson: Gson,
    ): SuggestGenrePagingSource = SuggestGenrePagingSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideRemoteGenreDatasource(
        client: OkHttpClient,
        gson: Gson,
        genre: SuggestGenrePagingSource,
    ): RemoteSetGenreDatasource = OkHttpSetGenreDatasource(
        client = client,
        gson = gson,
        genre = genre
    )

    @Provides
    @ViewModelScoped
    fun provideSuggestArtistRemotePageSource(
        client: OkHttpClient,
        gson: Gson,
    ): SuggestArtistPagingSource = SuggestArtistPagingSource(
        client = client,
        gson = gson
    )

    @Provides
    @ViewModelScoped
    fun provideRemoteSetArtistDatasource(
        client: OkHttpClient,
        gson: Gson,
        artist: SuggestArtistPagingSource,
    ): RemoteSetArtistDatasource = OkHttpSetArtistDatasource(
        client = client,
        gson = gson,
        artist = artist
    )
}