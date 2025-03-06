package com.poulastaa.view.presentation.artist

import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.view.domain.model.ViewArtistAllowedNavigationScreen

internal sealed interface ViewArtistUiEvent {
    data class EmitToast(val message: UiText) : ViewArtistUiEvent
    data class Navigate(val screen: ViewArtistAllowedNavigationScreen) : ViewArtistUiEvent
}