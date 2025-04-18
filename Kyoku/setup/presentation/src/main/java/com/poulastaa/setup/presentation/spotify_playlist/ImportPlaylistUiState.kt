package com.poulastaa.setup.presentation.spotify_playlist

import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.model.UiDetailedPrevSong

data class ImportPlaylistUiState(
    val isMakingApiCall: Boolean = false,
    val header: String = "",
    val link: TextHolder = TextHolder(),
    val data: List<UiPrevPlaylist> = emptyList(),
) {
    val floatActionButtonText: Int = if (data.isEmpty()) R.string.skip else R.string.continue_text
}

data class UiPrevPlaylist(
    val playlist: UiPlaylist = UiPlaylist(),
    val songs: List<UiDetailedPrevSong> = emptyList(),
    val isExpanded: Boolean = false,
)

data class UiPlaylist(
    val id: Long = -1,
    val name: String = "",
    val totalSongs: Int = 0,
)