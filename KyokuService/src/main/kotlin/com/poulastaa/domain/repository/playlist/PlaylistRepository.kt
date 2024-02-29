package com.poulastaa.domain.repository.playlist

import com.poulastaa.data.model.PlaylistRow

interface PlaylistRepository {
    suspend fun cretePlaylistForEmailUser(playlist: List<PlaylistRow> , playlistName:String)
    suspend fun cretePlaylistForGoogleUser(playlist: List<PlaylistRow>, playlistName:String)
    suspend fun cretePlaylistForPasskeyUser(playlist: List<PlaylistRow>, playlistName:String)

    fun getPlaylist()
}