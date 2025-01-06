package com.poulastaa.setup.presentation.spotify_playlist

import com.poulastaa.core.presentation.ui.UiText

sealed interface ImportPlaylistUiEvent {
    data object OnSuccess : ImportPlaylistUiEvent
    data object NavigateToBDate : ImportPlaylistUiEvent
    data class EmitToast(val message: UiText) : ImportPlaylistUiEvent
}