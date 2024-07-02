package com.poulastaa.setup.presentation.set_genre

import com.poulastaa.setup.presentation.set_genre.model.UiGenre

data class GenreUiState(
    val isMakingApiCall: Boolean = false,
    val isToastVisible: Boolean = true,
    val canMakeApiCall: Boolean = false,
    val data: List<UiGenre> = emptyList(),
)
