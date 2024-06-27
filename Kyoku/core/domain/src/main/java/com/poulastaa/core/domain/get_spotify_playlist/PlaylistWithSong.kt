package com.poulastaa.core.domain.get_spotify_playlist

data class PlaylistWithSong(
    val playlistId: Long,
    val name: String,
    val listOfPlaylistSong: List<PlaylistSong>,
)