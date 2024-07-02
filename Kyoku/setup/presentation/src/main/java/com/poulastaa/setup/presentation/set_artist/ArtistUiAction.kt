package com.poulastaa.setup.presentation.set_artist

import com.poulastaa.core.presentation.ui.UiText

sealed interface ArtistUiAction {
    data class EmitToast(val message: UiText) : ArtistUiAction
    data object NavigateToHome : ArtistUiAction
}