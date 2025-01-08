package com.poulastaa.core.database.repository

import android.util.Log
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomLocalImportPlaylistDatasource : LocalImportPlaylistDatasource {
    override fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>> {
        return flow { }
    }

    override suspend fun storePlaylist(playlist: DtoFullPlaylist) {
        Log.d("playlist" , playlist.toString())
    }
}