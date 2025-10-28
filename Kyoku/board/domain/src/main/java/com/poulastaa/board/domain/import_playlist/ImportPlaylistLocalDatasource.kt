package com.poulastaa.board.domain.import_playlist

import com.poulastaa.board.domain.import_playlist.model.DtoPrevPlaylist
import com.poulastaa.core.domain.model.DtoFullPlaylist
import kotlinx.coroutines.flow.Flow

interface ImportPlaylistLocalDatasource {
    suspend fun savePlaylist(playlist: DtoFullPlaylist)
    fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>>
}