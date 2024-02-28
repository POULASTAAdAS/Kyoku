package com.poulastaa.kyoku.data.model.screens.setup.suggest_artist

import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.UiArtist

data class SuggestArtistUiState(
    val maxGrid: Int = 3,
    val data: List<UiArtist> = emptyList(),
    val isInternetAvailable: Boolean = false,
    val isFirstApiCall: Boolean = true,
    val isAnyArtistLeft: Boolean = true,
    val isSendingDataToApi: Boolean = false,

    val isCookie: Boolean = false,
    val hearValue: String = ""
)
