package com.poulastaa.setup.presentation.get_spotify_playlist

import com.poulastaa.core.presentation.ui.UiText

sealed interface SpotifyUiAction {
    data class EmitToast(val message: UiText) : SpotifyUiAction
    data object NavigateToSetBDate : SpotifyUiAction
}