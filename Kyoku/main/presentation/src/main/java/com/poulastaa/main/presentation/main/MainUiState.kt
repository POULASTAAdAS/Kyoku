package com.poulastaa.main.presentation.main

import com.poulastaa.core.presentation.ui.model.UiUser
import com.poulastaa.main.domain.model.AppDrawerState

data class MainUiState(
    val drawerState: AppDrawerState = AppDrawerState.CLOSED,
    val user: UiUser = UiUser(),
)