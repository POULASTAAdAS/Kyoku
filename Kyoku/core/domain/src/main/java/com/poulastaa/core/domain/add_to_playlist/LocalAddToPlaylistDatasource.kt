package com.poulastaa.core.domain.add_to_playlist

import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevSavedPlaylist

interface LocalAddToPlaylistDatasource {
    suspend fun checkIfSongInFev(songId: Long): Boolean
    suspend fun getTotalSongsInFev(): Int
    suspend fun getPlaylistData(songId: Long): List<Pair<Pair<SIZE, PRESENT>, PrevSavedPlaylist>>

    suspend fun checkIfSongInDatabase(songId: Long): Boolean

    suspend fun editPlaylist(songId: Long, playlistIdList: Map<Long, Boolean>)
    suspend fun addSongToFavourite(songId: Long)
    suspend fun removeSongFromFavourite(songId: Long)

    suspend fun createPlaylist(data: PlaylistData)
}