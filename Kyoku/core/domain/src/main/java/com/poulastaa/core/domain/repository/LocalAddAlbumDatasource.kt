package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.AlbumId

interface LocalAddAlbumDatasource {
    suspend fun loadAllSavedAlbums(): List<AlbumId>
}