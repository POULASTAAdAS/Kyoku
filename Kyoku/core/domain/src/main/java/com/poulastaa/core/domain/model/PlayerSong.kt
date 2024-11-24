package com.poulastaa.core.domain.model

data class PlayerSong(
    val id: Int,
    val songId: Long = -1,
    val title: String = "",
    val artist: String = "",
    val coverImage: String = "",
    val masterPlaylistUrl: String = "",
    val isInFavourite: Boolean = false,
    val releaseYear: Int = -1,
    val primary: String = "",
    val secondary: String = "",
    val background: String = "",
)
