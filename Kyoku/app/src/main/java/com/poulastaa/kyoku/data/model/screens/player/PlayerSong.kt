package com.poulastaa.kyoku.data.model.screens.player

import androidx.compose.runtime.Stable

@Stable
data class PlayerSong(
    val id: Long = -1,
    val url: String = "",
    val masterPlaylist: String = "",
    val title: String = "",
    val artist: List<String> = listOf(""),
    val album: String = "",
    val year: String = "",
    val totalTime: String = ""
)