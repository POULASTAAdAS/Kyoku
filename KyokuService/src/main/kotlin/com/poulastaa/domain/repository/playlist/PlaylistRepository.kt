package com.poulastaa.domain.repository.playlist

import com.poulastaa.data.model.PlaylistRow

interface PlaylistRepository {
    suspend fun cretePlaylistForEmailUser(playlist: List<PlaylistRow>)
    suspend fun cretePlaylistForGoogleUser(playlist: List<PlaylistRow>)
    suspend fun cretePlaylistForPasskeyUser(playlist: List<PlaylistRow>)

    fun getPlaylist()
}