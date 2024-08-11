package com.poulastaa.play.presentation.root_drawer.home

import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.presentation.OtherScreens

sealed interface HomeUiAction {
    data class Navigate(val screen: OtherScreens) : HomeUiAction
    data class EmitToast(val message: UiText) : HomeUiAction
}