package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalSetArtistDatasource
import javax.inject.Inject

class RoomLocalSetArtistDatasource @Inject constructor(
    private val root: RootDao,
    private val ds: DatastoreRepository,
) : LocalSetArtistDatasource {
    override suspend fun storeArtist(list: List<DtoArtist>) {
        root.insertArtist(list.map { it.toEntityArtist() })
        ds.storeSignInState(SavedScreen.MAIN)
    }
}