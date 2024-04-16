package com.poulastaa.kyoku.data.model.screens.view_aritst

import androidx.compose.runtime.Stable

@Stable
data class ViewArtistUiState(
    val isCookie: Boolean = false,
    val headerValue: String = "",

    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isInternetError: Boolean = true,

    val noArtist: Boolean = false,

    val isErr: Boolean = false,

    val artist: List<ViewArtistUiArtist> = emptyList()
)

@Stable
data class ViewArtistUiArtist(
    val artistId: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val listened: Long = 0
)
