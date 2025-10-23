package com.poulastaa.board.presentation.import_playlist

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.designsystem.UiPrevPlaylistSong

@Stable
data class UiPreviewPlaylist(
    val id: Long = -1,
    val title: String = "",
    val songs: List<UiPrevPlaylistSong> = emptyList(),
    val isExpanded: Boolean = false,
)