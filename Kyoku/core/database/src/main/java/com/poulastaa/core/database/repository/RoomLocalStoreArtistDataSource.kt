package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.mapper.toArtistEntity
import com.poulastaa.core.domain.artist.LocalArtistDataSource
import com.poulastaa.core.domain.model.Artist
import javax.inject.Inject

class RoomLocalStoreArtistDataSource @Inject constructor(
    private val commonDao: CommonDao,
) : LocalArtistDataSource {
    override suspend fun insertArtist(artist: List<Artist>) {
        val entrys = artist.map {
            it.toArtistEntity()
        }

        commonDao.insertArtists(entrys)
    }
}