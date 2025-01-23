package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.mapper.toEntityGenre
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalSetGenreDatasource
import javax.inject.Inject

class RoomLocalSetGenreDatasource @Inject constructor(
    private val ds: DatastoreRepository,
    private val root: RootDao,
) : LocalSetGenreDatasource {
    override suspend fun storeGenreLocally(list: List<DtoGenre>) {
        ds.storeSignInState(SavedScreen.PIC_ARTIST)
        root.insertGenre(list.map { it.toEntityGenre() })
    }
}