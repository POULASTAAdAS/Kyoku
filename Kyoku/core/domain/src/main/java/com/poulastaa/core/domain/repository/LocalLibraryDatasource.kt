package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import kotlinx.coroutines.flow.Flow

interface LocalLibraryDatasource {
    suspend fun loadSavedPlaylist(): Flow<List<DtoPrevPlaylist>>
    fun loadSavedArtist(): Flow<List<DtoPrevArtist>>
    fun loadSavedAlbum(): Flow<List<DtoPrevAlbum>>
}