package com.poulastaa.domain.repository.playlist

import com.poulastaa.data.model.item.ItemOperation
import com.poulastaa.data.model.utils.PlaylistRow
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.domain.dao.playlist.Playlist

interface PlaylistRepository {
    suspend fun cretePlaylistForEmailUser(list: List<PlaylistRow>, playlist: Playlist)
    suspend fun cretePlaylistForGoogleUser(list: List<PlaylistRow>, playlist: Playlist)
    suspend fun cretePlaylistForPasskeyUser(list: List<PlaylistRow>, playlist: Playlist)

    suspend fun handlePlaylist(
        userId: Long,
        userType: UserType,
        playlistId: Long,
        operation: ItemOperation
    ): Boolean
}