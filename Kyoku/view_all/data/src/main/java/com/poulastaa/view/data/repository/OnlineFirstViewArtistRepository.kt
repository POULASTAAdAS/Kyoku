package com.poulastaa.view.data.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.repository.LocalViewArtistDatasource
import com.poulastaa.view.data.mapper.toDtoViewArtist
import com.poulastaa.view.domain.model.DtoViewArtisPayload
import com.poulastaa.view.domain.model.DtoViewArtist
import com.poulastaa.view.domain.repository.RemoteViewArtistDatasource
import com.poulastaa.view.domain.repository.ViewArtistRepository
import javax.inject.Inject

class OnlineFirstViewArtistRepository @Inject constructor(
    private val remote: RemoteViewArtistDatasource,
    private val local: LocalViewArtistDatasource,
) : ViewArtistRepository {
    override suspend fun loadArtist(
        artistId: ArtistId,
    ): Result<DtoViewArtisPayload<DtoViewArtist>, DataError.Network> =
        remote.loadArtist(artistId).map {
            DtoViewArtisPayload(
                artist = it.artist.toDtoViewArtist(local.isArtistFollowed(it.artist.id)),
                mostPopularSongs = it.mostPopularSongs
            )
        }
}