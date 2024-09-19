package com.poulastaa.play.presentation.player

import androidx.compose.ui.graphics.Color

data class PlayerUiSong(
    val index:Int = -1,
    val songId: Long = -1,
    val title: String = "",
    val artist: String = "",
    val coverImage: String = "",
    val masterPlaylistUrl: String = "",
    val album: String = "",
    val releaseYear: Int = -1,
    val colors: List<Color> = emptyList(),
    val isPlaying: Boolean = false,
    val isInFavourite: Boolean = false
)
