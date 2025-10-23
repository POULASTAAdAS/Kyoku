package com.poulastaa.core.presentation.designsystem

import androidx.compose.runtime.Stable

@Stable
data class UiPrevPlaylistSong(
    val id: Long,
    val title: String,
    val artist: String,
    val coverImage: String? = null,
)
