package com.poulastaa.core.domain.repository.view_edit

import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewEditType
import kotlinx.coroutines.flow.Flow

interface LocalViewEditDatasource {
    fun getSongs(playlistId: Long = -1, type: ViewEditType): Flow<List<PlaylistSong>>

    suspend fun deleteSong(playlistId: Long, songId: Long)
    suspend fun addSong(playlistId: Long, songId: Long)
    suspend fun getAllSongIds(playlistId: Long): List<Long>
}