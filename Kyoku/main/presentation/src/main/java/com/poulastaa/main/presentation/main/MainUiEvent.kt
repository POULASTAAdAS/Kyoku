package com.poulastaa.main.presentation.main

import com.poulastaa.core.domain.model.DtoCoreScreens
import com.poulastaa.core.presentation.designsystem.UiText

sealed interface MainUiEvent {
    data class EmitToast(val message: UiText) : MainUiEvent
    data class Navigate(val screen: DtoCoreScreens) : MainUiEvent
    data class NavigateMain(val screen: ScreensCore) : MainUiEvent
}