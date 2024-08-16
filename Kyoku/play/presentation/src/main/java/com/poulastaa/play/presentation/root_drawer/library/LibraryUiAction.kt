package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.ui.UiText

sealed interface LibraryUiAction {
    data class EmitToast(val message: UiText) : LibraryUiAction
    data class Navigate(val screen: ScreenEnum) : LibraryUiAction
}