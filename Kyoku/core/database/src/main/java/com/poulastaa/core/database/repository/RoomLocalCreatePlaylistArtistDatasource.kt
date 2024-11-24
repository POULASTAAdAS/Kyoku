package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.mapper.toArtist
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.repository.create_playlist.artist.LocalCreatePlaylistArtistDatasource
import javax.inject.Inject

class RoomLocalCreatePlaylistArtistDatasource @Inject constructor(
    private val commonDao: CommonDao,
) : LocalCreatePlaylistArtistDatasource {
    override suspend fun getArtist(artistId: Long): Artist? =
        commonDao.getArtistByIdOrNull(artistId)?.toArtist()
}