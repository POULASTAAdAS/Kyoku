package com.poulastaa.setup.presentation.pic_genre

import com.poulastaa.core.domain.model.GenreId
import com.poulastaa.core.presentation.ui.model.TextHolder

data class PicGenreUiState(
    val isMakingApiCall: Boolean = false,
    val searchGenre: TextHolder = TextHolder(),
    val data: List<GenreId> = emptyList(),
) {
    val isMinLimitReached = data.size >= 5
}


data class UiGenre(
    val id: Int,
    val name: String,
    val poster: String? = null,
    val isSelected: Boolean = false,
)