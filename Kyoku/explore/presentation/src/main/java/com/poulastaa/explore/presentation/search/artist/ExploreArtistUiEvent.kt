package com.poulastaa.explore.presentation.search.artist

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.UiText

sealed interface ExploreArtistUiEvent {
    data class EmitToast(val message: UiText) : ExploreArtistUiEvent
    data class NavigateToArtist(val artistId: ArtistId) : ExploreArtistUiEvent
}