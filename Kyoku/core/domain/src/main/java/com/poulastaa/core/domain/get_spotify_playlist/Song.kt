package com.poulastaa.core.domain.get_spotify_playlist

data class Song(
    val id: Long,
    val coverImage: String,
    val title: String,
    val artistName: String,
    val releaseYear: String,
    val masterPlaylistUrl: String,
    val primary: String,
    val background: String,
    val onBackground: String,
)
