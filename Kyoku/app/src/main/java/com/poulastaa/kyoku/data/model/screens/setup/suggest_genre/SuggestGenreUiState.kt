package com.poulastaa.kyoku.data.model.screens.setup.suggest_genre

import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponseStatus
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.UiGenre

data class SuggestGenreUiState(
    val isFirstReq: Boolean = true,
    val maxGrid: Int = 2,
    val data: List<UiGenre> = emptyList(),
    val selectedGenre: Int = 0,
    val apiResponse: SuggestGenreResponse = SuggestGenreResponse(
        status = SuggestGenreResponseStatus.FAILURE,
        genreList = emptyList()
    ),
    val isMakingApiCall: Boolean = true,
    val isInternetAvailable: Boolean = false,
    val isFirstCall: Boolean = true
)
