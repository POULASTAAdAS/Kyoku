package com.poulastaa.kyoku.data.model.screens.player

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.poulastaa.kyoku.ui.theme.dark_type_one_background
import com.poulastaa.kyoku.ui.theme.dark_type_two_tertiary

@Stable
data class PlayerSong(
    val id: Long = -1,
    val url: String = "",
    val masterPlaylist: String = "",
    val title: String = "",
    val artist: List<String> = listOf(""),
    val album: String = "",
    val year: String = "",
    val totalTime: String = "",
    val totalInMili: Float = 0f,
    val currentInMin: String = "-.-",
    val colorOne: Color = dark_type_two_tertiary,
    val colorTwo: Color = dark_type_one_background,
)