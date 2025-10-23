package com.poulastaa.board.presentation.import_playlist

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface ImportPlaylistUiEvent {
    data class EmitToast(val message: UiText) : ImportPlaylistUiEvent
    data object NavigateToSelectBDate : ImportPlaylistUiEvent
}