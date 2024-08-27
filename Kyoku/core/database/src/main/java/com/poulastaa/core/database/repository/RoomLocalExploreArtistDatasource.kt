package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.mapper.toArtist
import com.poulastaa.core.database.mapper.toArtistEntity
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.repository.explore_artist.LocalExploreArtistDatasource
import javax.inject.Inject

class RoomLocalExploreArtistDatasource @Inject constructor(
    private val commonDao: CommonDao
) : LocalExploreArtistDatasource {
    override suspend fun getArtist(artistId: Long): Artist? =
        commonDao.getArtistByIdOrNull(artistId)?.toArtist()

    override suspend fun followArtist(artist: Artist) =
        commonDao.insertArtist(artist.toArtistEntity())

    override suspend fun unFollowArtist(artistId: Long) = commonDao.deleteArtist(artistId)
}