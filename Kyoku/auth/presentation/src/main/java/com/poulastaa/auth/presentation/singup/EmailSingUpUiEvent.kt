package com.poulastaa.auth.presentation.singup

import com.poulastaa.auth.presentation.components.AuthAllowedNavigationScreen
import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface EmailSingUpUiEvent {
    data object OnNavigateBack : EmailSingUpUiEvent
    data object NavigateToSetUp : EmailSingUpUiEvent
    data class EmitToast(val message: UiText) : EmailSingUpUiEvent
}