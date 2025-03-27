package com.poulastaa.explore.presentation.search.all_from_artist

import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.explore.domain.model.ExploreAllowedNavigationScreen

internal sealed interface AllFromArtistUiEvent {
    data class EmitToast(val message: UiText) : AllFromArtistUiEvent
    data class Navigate(val screen: ExploreAllowedNavigationScreen) : AllFromArtistUiEvent
}