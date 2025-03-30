package com.poulastaa.main.presentation.home

import com.poulastaa.core.domain.model.ExploreScreenType
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface HomeUiEvent {
    data class EmitToast(val message: UiText) : HomeUiEvent
    data class NavigateToView(val type: ViewType, val otherId: Long) : HomeUiEvent
    data class NavigateToExplore(val type: ExploreScreenType) : HomeUiEvent
}