package com.poulastaa.main.presentation.main

import com.poulastaa.main.domain.model.AppDrawerScreen
import com.poulastaa.main.domain.model.AppNavigationRailScreen

sealed interface MainUiAction {
    data object ToggleDrawer : MainUiAction
    data object ToggleNavigationRail : MainUiAction
    data class NavigateToDrawerScreen(val screen: AppDrawerScreen) : MainUiAction
    data class NavigateToNavigationRailScreen(val screen: AppNavigationRailScreen) : MainUiAction
    data object NavigateBottomNavigationScreen : MainUiAction // todo make class and add home and library screen
}