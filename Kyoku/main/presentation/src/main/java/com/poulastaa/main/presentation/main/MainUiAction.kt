package com.poulastaa.main.presentation.main

import com.poulastaa.main.domain.model.AppDrawerScreen

sealed interface MainUiAction {
    data object ToggleDrawer : MainUiAction
    data class Navigate(val screen: AppDrawerScreen) : MainUiAction
}