package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.play.domain.SaveScreen

sealed interface RootDrawerUiEvent {
    data object OnDrawerToggle : RootDrawerUiEvent
    data class Navigate(val screen: ScreenEnum) : RootDrawerUiEvent
    data object LogOut : RootDrawerUiEvent

    data class SaveScreenToggle(val screen: SaveScreen) : RootDrawerUiEvent
}