package com.poulastaa.core.domain.repository.view_edit

import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface RemoteViewEditDatasource {
    suspend fun deleteSong(playlistId: Long, songId: Long): EmptyResult<DataError.Network>
    suspend fun addSong(playlistId: Long, songId: Long): Result<Song, DataError.Network>
}