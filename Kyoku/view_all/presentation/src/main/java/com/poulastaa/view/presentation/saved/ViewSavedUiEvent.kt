package com.poulastaa.view.presentation.saved

import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.view.domain.model.ViewSavedAllowedNavigationScreen

internal sealed interface ViewSavedUiEvent {
    data class EmitToast(val message: UiText) : ViewSavedUiEvent
    data class Navigate(val screen: ViewSavedAllowedNavigationScreen) : ViewSavedUiEvent
}