package com.poulastaa.core.domain.model

data class PlaylistWithSong(
    val playlist: Playlist,
    val songs: List<Song>,
)
