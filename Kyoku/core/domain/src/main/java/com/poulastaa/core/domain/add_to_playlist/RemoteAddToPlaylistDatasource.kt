package com.poulastaa.core.domain.add_to_playlist

interface RemoteAddToPlaylistDatasource {
    suspend fun addSongToPlaylist(songId: Long, playlistId: Long)
    suspend fun addSongToFavourite(songId: Long)
}