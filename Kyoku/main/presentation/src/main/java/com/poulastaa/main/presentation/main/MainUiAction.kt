package com.poulastaa.main.presentation.main

import androidx.compose.ui.geometry.Offset
import com.poulastaa.main.domain.model.AppDrawerScreen
import com.poulastaa.main.domain.model.AppNavigationBottomBarScreen
import com.poulastaa.main.domain.model.AppNavigationRailScreen

sealed interface MainUiAction {
    data object ToggleDrawer : MainUiAction
    data object ToggleNavigationRail : MainUiAction
    data class NavigateToDrawerScreen(
        val screen: AppDrawerScreen,
        val offset: Offset? = null,
    ) : MainUiAction

    data class NavigateToNavigationRailScreen(
        val screen: AppNavigationRailScreen,
        val offset: Offset? = null,
    ) : MainUiAction

    data class NavigateBottomBarScreen(val screen: AppNavigationBottomBarScreen) : MainUiAction
    data object ResetRevelAnimation : MainUiAction
}