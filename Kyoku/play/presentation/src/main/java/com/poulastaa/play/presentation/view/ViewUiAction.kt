package com.poulastaa.play.presentation.view

import com.poulastaa.core.domain.ScreenEnum

sealed interface ViewUiAction {
    data class Navigate(val screen: ScreenEnum) : ViewUiAction
}