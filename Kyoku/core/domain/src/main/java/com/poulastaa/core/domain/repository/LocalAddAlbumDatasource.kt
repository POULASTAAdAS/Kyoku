package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.DtoFullAlbum

interface LocalAddAlbumDatasource {
    suspend fun loadAllSavedAlbums(): List<AlbumId>
    suspend fun saveAlbums(list: List<DtoFullAlbum>)
}