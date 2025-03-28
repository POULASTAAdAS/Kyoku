package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.ExploreDao
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.repository.LocalAllFromArtistDatasource
import javax.inject.Inject

internal class RoomLocalAllFromArtistDatasource @Inject constructor(
    private val dao: ExploreDao,
) : LocalAllFromArtistDatasource {
    override suspend fun getArtist(artistId: ArtistId): DtoPrevArtist? =
        dao.getArtistOnId(artistId)?.toDtoPrevArtist()
            ?: dao.getPrevArtistOnId(artistId)?.toDtoPrevArtist()
}