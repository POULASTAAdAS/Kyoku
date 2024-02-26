package com.poulastaa.kyoku.data.model.screens.setup.suggest_genre

import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.UiGenre

data class SuggestGenreUiState(
    val maxGrid: Int = 2,
    val data: List<UiGenre> = emptyList(),
    val isInternetAvailable: Boolean = false,
    val isFirstApiCall: Boolean = true,
    val isAnyGenreLeft: Boolean = true,
    val isSendingDataToApi:Boolean = false
)
