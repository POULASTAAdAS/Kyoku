package com.poulastaa.setup.presentation.set_artist

import com.poulastaa.core.presentation.ui.model.SpotifyUiArtist

data class ArtistUiState(
    val isMakingApiCall: Boolean = false,
    val isInternetErr: Boolean = false,

    val header: String = "",

    val data: List<SpotifyUiArtist> = emptyList(),

    val isToastVisible: Boolean = true,
    val canMakeApiCall: Boolean = false,
)
