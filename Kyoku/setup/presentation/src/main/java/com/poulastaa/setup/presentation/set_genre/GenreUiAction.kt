package com.poulastaa.setup.presentation.set_genre

import com.poulastaa.core.presentation.ui.UiText

sealed interface GenreUiAction {
    data class EmitToast(val message: UiText) : GenreUiAction
    data object NavigateToSetArtist : GenreUiAction
}