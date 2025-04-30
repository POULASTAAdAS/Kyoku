package com.poulastaa.add.presentation.album.view_album

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface AddAlbumViewUiEvent {
    data class EmitToast(val message: UiText) : AddAlbumViewUiEvent
}
