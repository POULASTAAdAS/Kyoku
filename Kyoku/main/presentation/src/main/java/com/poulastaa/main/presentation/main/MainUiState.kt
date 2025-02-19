package com.poulastaa.main.presentation.main

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import com.poulastaa.core.presentation.designsystem.model.UiUser
import com.poulastaa.main.domain.model.AppNavigationBottomBarScreen
import com.poulastaa.main.domain.model.AppNavigationDrawerState
import com.poulastaa.main.domain.model.AppNavigationRailScreen
import com.poulastaa.main.domain.model.AppNavigationRailState

@Stable
data class MainUiState(
    val navigationDrawerState: AppNavigationDrawerState = AppNavigationDrawerState.CLOSED,
    val navigationRailState: AppNavigationRailState = AppNavigationRailState.CLOSED,
    val navigationRailScreen: AppNavigationRailScreen = AppNavigationRailScreen.HOME,
    val navigationBottomBarScreen: AppNavigationBottomBarScreen = AppNavigationBottomBarScreen.HOME,
    val user: UiUser = UiUser(),
    val greetings: String = "Hello",

    val offset: Offset = IntOffset(0, 0).toOffset(),
    val themChangeAnimationTime: Int = 800,
)