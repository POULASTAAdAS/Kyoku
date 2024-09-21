package com.poulastaa.core.domain.repository.add_to_playlist

import com.poulastaa.core.domain.model.PrevSavedPlaylist
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult

typealias SIZE = Int
typealias PRESENT = Boolean

interface AddToPlaylistRepository {
    suspend fun checkIfSongInFev(songId: Long): Boolean
    suspend fun getTotalSongsInFev(): Int
    suspend fun getPlaylistData(songId: Long): List<Pair<Pair<SIZE, PRESENT>, PrevSavedPlaylist>>

    suspend fun saveSong(songId: Long): EmptyResult<DataError.Network>

    suspend fun editPlaylist(
        songId: Long,
        playlistIdList: Map<Long, Boolean>,
    ): EmptyResult<DataError.Network>

    suspend fun addSongToFavourite(songId: Long): EmptyResult<DataError.Network>
    suspend fun removeSongFromFavourite(songId: Long): EmptyResult<DataError.Network>

    suspend fun createPlaylist(
        songId: Long,
        name: String,
    ): EmptyResult<DataError.Network>
}