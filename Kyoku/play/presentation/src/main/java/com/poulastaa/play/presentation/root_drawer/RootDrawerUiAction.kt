package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.ui.UiText

sealed interface RootDrawerUiAction {
    data class EmitToast(val message: UiText) : RootDrawerUiAction
    data class Navigate(val screen: ScreenEnum) : RootDrawerUiAction
}