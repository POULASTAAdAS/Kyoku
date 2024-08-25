package com.poulastaa.play.presentation.add_to_playlist

import com.poulastaa.core.presentation.ui.UiText

sealed interface AddToPlaylistUiAction {
    data class EmitToast(val message: UiText) : AddToPlaylistUiAction
    data object NavigateBack : AddToPlaylistUiAction
}