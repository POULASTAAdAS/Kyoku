package com.poulastaa.setup.network.model

import com.poulastaa.setup.domain.model.DtoFullPlaylist
import kotlinx.serialization.Serializable

@Serializable
data class FullPlaylistResponse(
    val playlist: PlaylistResponse,
    val songs: List<SongResponse> = emptyList(),
) {
    fun toDto() = DtoFullPlaylist(
        playlist = playlist.toDto(),
        songs = songs.map(SongResponse::toDto),
    )
}
