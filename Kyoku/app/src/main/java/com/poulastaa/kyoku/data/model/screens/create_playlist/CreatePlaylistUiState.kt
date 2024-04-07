package com.poulastaa.kyoku.data.model.screens.create_playlist

import androidx.compose.runtime.Stable

@Stable
data class CreatePlaylistUiState(
    val text: String = "",
    val isInternetAvailable: Boolean = false,
    val isInternetError: Boolean = false,

    val isLoading: Boolean = true,
    val isCriticalErr: Boolean = false,

    val songIdList: List<Long> = emptyList(),

)
