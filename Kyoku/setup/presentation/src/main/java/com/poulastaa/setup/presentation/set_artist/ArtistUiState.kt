package com.poulastaa.setup.presentation.set_artist

import com.poulastaa.setup.presentation.set_artist.model.UiArtist

data class ArtistUiState(
    val isMakingApiCall: Boolean = false,
    val isInternetErr: Boolean = false,

    val header: String = "",

    val data: List<UiArtist> = emptyList(),

    val isToastVisible: Boolean = false,
    val canMakeApiCall: Boolean = false,
)
