package com.poulastaa.play.presentation.create_playlist

import com.poulastaa.core.presentation.ui.UiText

sealed interface CreatePlaylistUiAction {
    data class EmitToast(val message: UiText) : CreatePlaylistUiAction
}