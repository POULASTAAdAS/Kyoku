package com.poulastaa.view.presentation.others

import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.view.domain.model.ViewOtherAllowedNavigationScreen

internal sealed interface ViewOtherUiEvent {
    data class EmitToast(val message: UiText) : ViewOtherUiEvent
    data class Navigate(val screens: ViewOtherAllowedNavigationScreen) : ViewOtherUiEvent
}