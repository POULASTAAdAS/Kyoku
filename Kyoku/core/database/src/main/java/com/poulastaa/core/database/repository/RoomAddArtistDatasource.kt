package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.entity.EntitySavedArtist
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.repository.LocalAddArtistDatasource
import kotlinx.coroutines.flow.first

internal class RoomAddArtistDatasource(
    private val root: RootDao,
    private val libraryDao: LibraryDao,
) : LocalAddArtistDatasource {
    override suspend fun loadSavedArtist(): List<ArtistId> =
        libraryDao.loadSavedPrevArtist().first().map { it.id }

    override suspend fun savedArtist(list: List<DtoArtist>) {
        root.insertArtist(list.map { it.toEntityArtist() }).also {
            root.insertSavedArtist(list.map { EntitySavedArtist(it.id) })
        }
    }
}