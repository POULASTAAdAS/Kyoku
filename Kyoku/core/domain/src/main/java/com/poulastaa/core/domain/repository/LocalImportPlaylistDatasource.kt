package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import kotlinx.coroutines.flow.Flow

interface LocalImportPlaylistDatasource {
    fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>>
    suspend fun storePlaylist(playlist: DtoFullPlaylist)
}