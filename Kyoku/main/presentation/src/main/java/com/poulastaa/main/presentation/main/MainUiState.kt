package com.poulastaa.main.presentation.main

import com.poulastaa.core.presentation.ui.model.UiUser
import com.poulastaa.main.domain.model.AppDrawerState
import com.poulastaa.main.domain.model.AppNavigationRailScreen
import com.poulastaa.main.domain.model.AppNavigationRailState

data class MainUiState(
    val drawerState: AppDrawerState = AppDrawerState.CLOSED,
    val navigationRailState: AppNavigationRailState = AppNavigationRailState.CLOSED,
    val navigationRailScreen: AppNavigationRailScreen = AppNavigationRailScreen.HOME,
    val user: UiUser = UiUser(),
)