package com.poulastaa.play.presentation.view

import com.poulastaa.core.presentation.ui.UiText

sealed interface ViewUiAction {
    data class EmitToast(val message: UiText) : ViewUiAction
    data class Navigate(val screen: ViewOtherScreen) : ViewUiAction
}