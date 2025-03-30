package com.poulastaa.explore.presentation.search

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface ExploreAlbumUiEvent {
    data class EmitToast(val message: UiText) : ExploreAlbumUiEvent
    data class NavigateToAlbum(val albumId: AlbumId) : ExploreAlbumUiEvent
}