package com.poulastaa.setup.data.di

import com.poulastaa.core.domain.repository.LocalBDateDatasource
import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
import com.poulastaa.core.domain.repository.LocalSetArtistDatasource
import com.poulastaa.core.domain.repository.LocalSetGenreDatasource
import com.poulastaa.setup.data.repository.import_playlist.OnlineFirstImportPlaylistRepository
import com.poulastaa.setup.data.repository.import_playlist.UseCaseValidatePlaylistLink
import com.poulastaa.setup.data.repository.set_artist.OnlineFirstSetArtistRepository
import com.poulastaa.setup.data.repository.set_bdate.OnlineFirstBDateRepository
import com.poulastaa.setup.data.repository.set_bdate.UseCaseValidateBDate
import com.poulastaa.setup.data.repository.set_genre.OnlineFirstSetGenreRepository
import com.poulastaa.setup.domain.repository.import_playlist.ImportPlaylistRepository
import com.poulastaa.setup.domain.repository.import_playlist.RemoteImportPlaylistDatasource
import com.poulastaa.setup.domain.repository.import_playlist.SpotifyPlaylistLinkValidator
import com.poulastaa.setup.domain.repository.set_artist.RemoteSetArtistDatasource
import com.poulastaa.setup.domain.repository.set_artist.SetArtistRepository
import com.poulastaa.setup.domain.repository.set_bdate.BDateRepository
import com.poulastaa.setup.domain.repository.set_bdate.BDateValidator
import com.poulastaa.setup.domain.repository.set_bdate.RemoteBDateDatasource
import com.poulastaa.setup.domain.repository.set_genre.RemoteSetGenreDatasource
import com.poulastaa.setup.domain.repository.set_genre.SetGenreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SetupDataModule {
    @Provides
    @ViewModelScoped
    fun provideSpotifyLinkValidator(): SpotifyPlaylistLinkValidator = UseCaseValidatePlaylistLink()

    @Provides
    @ViewModelScoped
    fun provideImportPlaylistRepository(
        local: LocalImportPlaylistDatasource,
        remote: RemoteImportPlaylistDatasource,
    ): ImportPlaylistRepository = OnlineFirstImportPlaylistRepository(
        local = local,
        remote = remote
    )

    @Provides
    @ViewModelScoped
    fun provideBDateValidator(): BDateValidator = UseCaseValidateBDate()


    @Provides
    @ViewModelScoped
    fun provideBDateRepository(
        local: LocalBDateDatasource,
        remote: RemoteBDateDatasource,
    ): BDateRepository = OnlineFirstBDateRepository(
        local = local,
        remote = remote
    )

    @Provides
    @ViewModelScoped
    fun provideSetGenreRepository(
        remote: RemoteSetGenreDatasource,
        local: LocalSetGenreDatasource,
    ): SetGenreRepository = OnlineFirstSetGenreRepository(
        remote = remote,
        local = local
    )

    @Provides
    @ViewModelScoped
    fun provideSetArtistRepository(
        remote: RemoteSetArtistDatasource,
        local: LocalSetArtistDatasource,
    ): SetArtistRepository = OnlineFirstSetArtistRepository(
        remote = remote,
        local = local
    )
}