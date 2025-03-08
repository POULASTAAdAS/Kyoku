package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.repository.LocalViewArtistDatasource
import javax.inject.Inject

internal class RoomLocalViewArtistDatasource @Inject constructor(
    private val view: ViewDao,
) : LocalViewArtistDatasource {
    override suspend fun isArtistFollowed(artistId: ArtistId): Boolean =
        view.isArtistFollowed(artistId)
}