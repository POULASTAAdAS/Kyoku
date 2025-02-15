package com.poulastaa.main.presentation.main

import com.poulastaa.main.domain.model.AppNavigationBottomBarScreen
import com.poulastaa.main.domain.model.AppDrawerScreen
import com.poulastaa.main.domain.model.AppNavigationRailScreen

sealed interface MainUiAction {
    data object ToggleDrawer : MainUiAction
    data object ToggleNavigationRail : MainUiAction
    data class NavigateToDrawerScreen(val screen: AppDrawerScreen) : MainUiAction
    data class NavigateToNavigationRailScreen(val screen: AppNavigationRailScreen) : MainUiAction
    data class NavigateBottomBarScreen(val screen: AppNavigationBottomBarScreen) : MainUiAction
}