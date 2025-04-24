package com.poulastaa.add.presentation.artist

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface AddArtistUiEvent {
    data class EmitToast(val message: UiText) : AddArtistUiEvent
}