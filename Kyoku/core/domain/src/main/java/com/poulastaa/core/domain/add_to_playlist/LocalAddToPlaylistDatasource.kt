package com.poulastaa.core.domain.add_to_playlist

import com.poulastaa.core.domain.model.PrevSavedPlaylist
import kotlinx.coroutines.flow.Flow

interface LocalAddToPlaylistDatasource {
    suspend fun checkIfSongInFev(songId: Long): Boolean
    suspend fun getTotalSongsInFev(): Int
    suspend fun getPlaylistData(): Flow<List<Pair<Int, PrevSavedPlaylist>>>

    suspend fun addSongToPlaylist(songId: Long, playlistId: Long)
    suspend fun addSongToFavourite(songId: Long)
}