package com.poulastaa.board.domain.import_playlist

import com.poulastaa.board.domain.import_playlist.model.DtoPrevPlaylist
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ImportPlaylistRepository {
    suspend fun requestPlaylist(playlistId: String): EmptyResult<DataError.Network>
    fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>>
}