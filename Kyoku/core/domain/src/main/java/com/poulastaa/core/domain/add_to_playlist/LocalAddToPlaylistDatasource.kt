package com.poulastaa.core.domain.add_to_playlist

import com.poulastaa.core.domain.model.PrevSavedPlaylist

interface LocalAddToPlaylistDatasource {
    suspend fun checkIfSongInFev(songId: Long): Boolean
    suspend fun getTotalSongsInFev(): Int
    suspend fun getPlaylistData(songId: Long): List<Pair<Pair<SIZE, PRESENT>, PrevSavedPlaylist>>


    suspend fun addSongToPlaylist(songId: Long, playlistId: Long)
    suspend fun addSongToFavourite(songId: Long)
}