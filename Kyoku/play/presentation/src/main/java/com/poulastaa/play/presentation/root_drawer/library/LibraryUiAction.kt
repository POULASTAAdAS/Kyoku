package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.core.domain.ScreenEnum

sealed interface LibraryUiAction {
    data class Navigate(val screen: ScreenEnum) : LibraryUiAction
}