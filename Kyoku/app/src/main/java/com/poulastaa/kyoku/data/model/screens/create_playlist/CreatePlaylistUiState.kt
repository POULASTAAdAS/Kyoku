package com.poulastaa.kyoku.data.model.screens.create_playlist

import androidx.compose.runtime.Stable
import com.poulastaa.kyoku.data.model.screens.home.HomeLongClickType

@Stable
data class CreatePlaylistUiState(
    val id: Long = -1,
    val text: String = "",
    val type: HomeLongClickType = HomeLongClickType.ARTIST_MIX,
    val isInternetAvailable: Boolean = false,
    val isInternetError: Boolean = false,

    val isLoading: Boolean = true,
    val isCriticalErr: Boolean = false,

    val songIdList: List<Long> = emptyList()
)
