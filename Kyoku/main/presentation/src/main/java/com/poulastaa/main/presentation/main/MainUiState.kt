package com.poulastaa.main.presentation.main

import com.poulastaa.core.presentation.designsystem.model.UiUser
import com.poulastaa.main.domain.model.AppNavigationBottomBarScreen
import com.poulastaa.main.domain.model.AppNavigationDrawerState
import com.poulastaa.main.domain.model.AppNavigationRailScreen
import com.poulastaa.main.domain.model.AppNavigationRailState

data class MainUiState(
    val navigationDrawerState: AppNavigationDrawerState = AppNavigationDrawerState.CLOSED,
    val navigationRailState: AppNavigationRailState = AppNavigationRailState.CLOSED,
    val navigationRailScreen: AppNavigationRailScreen = AppNavigationRailScreen.HOME,
    val navigationBottomBarScreen: AppNavigationBottomBarScreen = AppNavigationBottomBarScreen.HOME,
    val user: UiUser = UiUser(),
)