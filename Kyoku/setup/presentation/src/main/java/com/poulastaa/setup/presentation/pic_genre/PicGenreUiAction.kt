package com.poulastaa.setup.presentation.pic_genre

import com.poulastaa.core.domain.model.GenreId

sealed interface PicGenreUiAction {
    data class OnGenreChange(val genre: String) : PicGenreUiAction
    data class OnGenreSelect(val id: GenreId) : PicGenreUiAction
    data object OnContinueClick : PicGenreUiAction
}