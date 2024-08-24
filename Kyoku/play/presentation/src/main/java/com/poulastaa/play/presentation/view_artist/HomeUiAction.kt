package com.poulastaa.play.presentation.view_artist

import com.poulastaa.core.presentation.ui.UiText

sealed interface ViewArtistUiAction {
    data class EmitToast(val message: UiText) : ViewArtistUiAction
}