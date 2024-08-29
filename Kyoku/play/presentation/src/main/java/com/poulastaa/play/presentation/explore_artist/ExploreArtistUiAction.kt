package com.poulastaa.play.presentation.explore_artist

import com.poulastaa.core.presentation.ui.UiText

sealed interface ExploreArtistUiAction {
    data class EmitToast(val message: UiText) : ExploreArtistUiAction
    data class Navigate(val screen: ExploreArtistOtherScreen) : ExploreArtistUiAction
}