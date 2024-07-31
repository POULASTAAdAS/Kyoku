package com.poulastaa.play.presentation.root_drawer.home

import com.poulastaa.core.presentation.ui.UiText

sealed interface HomeUiAction {
    data class EmitToast(val message: UiText) : HomeUiAction
}