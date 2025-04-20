package com.poulastaa.main.domain.model

import com.poulastaa.core.domain.model.ExploreScreenType
import com.poulastaa.core.domain.model.ViewType

sealed interface MainAllowedNavigationScreens {
    data class NavigateToView(
        val type: ViewType,
        val otherId: Long,
    ) : MainAllowedNavigationScreens

    data class NavigateToExplore(val type: ExploreScreenType) : MainAllowedNavigationScreens

    data class NavigateToEditSavedItemScreen(
        val type: PayloadSaveItemType,
    ) : MainAllowedNavigationScreens
}