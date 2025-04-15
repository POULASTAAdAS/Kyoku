package com.poulastaa.core.presentation.ui.components.crate_playlist

import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface CreatePlaylistUiEvent {
    data class EmitToast(val message: UiText) : CreatePlaylistUiEvent
    data class Created(val playlistId: PlaylistId) : CreatePlaylistUiEvent
    data object Canceled : CreatePlaylistUiEvent
}