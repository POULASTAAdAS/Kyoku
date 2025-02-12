package com.poulastaa.setup.domain.repository.import_playlist

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import kotlinx.coroutines.flow.Flow

interface ImportPlaylistRepository {
    fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>>
    suspend fun importPlaylist(playlistId: String): EmptyResult<DataError.Network>
}