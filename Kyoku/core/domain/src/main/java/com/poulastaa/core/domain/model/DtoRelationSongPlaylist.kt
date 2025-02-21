package com.poulastaa.core.domain.model

data class DtoRelationSongPlaylist(
    val playlistId: PlaylistId,
    val list: List<SongId>,
)
