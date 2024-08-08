package com.poulastaa.play.presentation.add_as_playlist

import com.poulastaa.core.presentation.ui.UiText

sealed interface AddAsPlaylistUiAction {
    data class EmitToast(val message: UiText) : AddAsPlaylistUiAction
    data object Success : AddAsPlaylistUiAction
    data object Cancel : AddAsPlaylistUiAction
}