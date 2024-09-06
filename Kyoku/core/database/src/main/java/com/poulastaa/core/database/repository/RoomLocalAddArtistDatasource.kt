package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.mapper.toArtistEntity
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.repository.new_artist.LocalNewArtistDataSource
import javax.inject.Inject

class RoomLocalAddArtistDatasource @Inject constructor(
    private val commonDao: CommonDao
) : LocalNewArtistDataSource {
    override suspend fun saveArtist(list: List<Artist>) {
        list.map { it.toArtistEntity() }
            .let { commonDao.insertArtists(it) }
    }
}