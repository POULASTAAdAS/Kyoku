package com.poulastaa.kyoku.data.model.screens.song_view

import androidx.compose.runtime.Stable

@Stable
data class ArtistAllUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isInternetError: Boolean = true,
    val errorMessage: String = "Please Check Your Internet Connection.",
    val isCooke: Boolean = false,
    val headerValue: String = ""
)
