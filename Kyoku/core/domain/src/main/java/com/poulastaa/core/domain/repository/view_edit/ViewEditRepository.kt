package com.poulastaa.core.domain.repository.view_edit

import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewEditType
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ViewEditRepository {
    fun getSongs(playlistId: Long = -1, type: ViewEditType): Flow<List<PlaylistSong>>

    suspend fun deleteSong(playlistId: Long, songId: Long): EmptyResult<DataError.Network>
    suspend fun addSong(playlistId: Long, songId: Long): EmptyResult<DataError.Network>
}