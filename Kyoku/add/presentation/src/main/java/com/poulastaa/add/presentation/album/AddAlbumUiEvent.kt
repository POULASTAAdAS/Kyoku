package com.poulastaa.add.presentation.album

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface AddAlbumUiEvent {
    data class EmitToast(val message: UiText) : AddAlbumUiEvent
}