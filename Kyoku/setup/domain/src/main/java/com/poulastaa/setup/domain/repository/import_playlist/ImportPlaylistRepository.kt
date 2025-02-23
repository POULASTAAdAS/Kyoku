package com.poulastaa.setup.domain.repository.import_playlist

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.model.DtoPrevFullPlaylist
import kotlinx.coroutines.flow.Flow

interface ImportPlaylistRepository {
    fun loadAllPlaylist(): Flow<List<DtoPrevFullPlaylist>>
    suspend fun importPlaylist(playlistId: String): EmptyResult<DataError.Network>
}