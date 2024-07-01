package com.poulastaa.setup.presentation.set_b_date

import com.poulastaa.core.presentation.ui.UiText

sealed interface BDateUiAction {
    data class EmitToast(val message: UiText) : BDateUiAction
    data object NavigateToSpotifyPlaylist : BDateUiAction
    data object NavigateToSetGenre : BDateUiAction
}