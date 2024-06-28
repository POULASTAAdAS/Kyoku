package com.poulastaa.core.domain.model

data class PlaylistWithSongInfo(
    val playlistId: Long,
    val name: String,
    val listOfPlaylistSong: List<PlaylistSong>,
)