package com.poulastaa.auth.presentation.intro

import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.core.domain.SavedScreen
import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface IntroUiEvent {
    data class EmitToast(val message: UiText) : IntroUiEvent
    data class GoogleAuthSuccess(val screen: SavedScreen) : IntroUiEvent
    data class Navigate(val screen: IntroAllowedNavigationScreens) : IntroUiEvent
}