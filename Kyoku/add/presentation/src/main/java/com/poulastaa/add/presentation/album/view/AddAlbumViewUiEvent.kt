package com.poulastaa.add.presentation.album.view

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface AddAlbumViewUiEvent {
    data class EmitToast(val message: UiText) : AddAlbumViewUiEvent
}
