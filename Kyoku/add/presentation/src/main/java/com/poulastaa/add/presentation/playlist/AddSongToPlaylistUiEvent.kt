package com.poulastaa.add.presentation.playlist

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface AddSongToPlaylistUiEvent {
    data class EmitToast(val message: UiText) : AddSongToPlaylistUiEvent
}