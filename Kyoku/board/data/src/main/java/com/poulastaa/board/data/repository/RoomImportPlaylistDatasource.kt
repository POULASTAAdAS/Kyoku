package com.poulastaa.board.data.repository

import com.poulastaa.board.domain.import_playlist.ImportPlaylistLocalDatasource
import com.poulastaa.board.domain.import_playlist.model.DtoPrevPlaylist
import com.poulastaa.core.domain.model.DtoFullPlaylist
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

internal class RoomImportPlaylistDatasource @Inject constructor() : ImportPlaylistLocalDatasource {
    override suspend fun savePlaylist(playlist: DtoFullPlaylist) {
        // todo save playlist
        // simulating save delay
        delay(3000)
    }

    override fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>> = emptyFlow()
}