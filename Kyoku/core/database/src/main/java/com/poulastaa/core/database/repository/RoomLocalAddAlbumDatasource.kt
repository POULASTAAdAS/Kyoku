package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.repository.LocalAddAlbumDatasource
import javax.inject.Inject

internal class RoomLocalAddAlbumDatasource @Inject constructor(
    private val root: RootDao,
) : LocalAddAlbumDatasource {
    override suspend fun loadAllSavedAlbums(): List<AlbumId> = root.getSavedAlbumIdList()
    override suspend fun saveAlbums(list: List<DtoFullAlbum>) {
        TODO("Not yet implemented")
    }
}