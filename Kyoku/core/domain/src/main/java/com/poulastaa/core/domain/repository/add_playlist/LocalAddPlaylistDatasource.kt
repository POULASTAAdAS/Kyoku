package com.poulastaa.core.domain.repository.add_playlist

import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevSong

interface LocalAddPlaylistDatasource {
    suspend fun getSongId(type: ExploreType): List<PrevSong>
    suspend fun isNameConflict(name: String): Boolean
    suspend fun savePlaylist(playlist: PlaylistData)
}