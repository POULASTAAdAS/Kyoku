package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoPrevFullPlaylist
import kotlinx.coroutines.flow.Flow

interface LocalImportPlaylistDatasource {
    fun loadAllPlaylist(): Flow<List<DtoPrevFullPlaylist>>
    suspend fun storePlaylist(playlist: DtoFullPlaylist)
}