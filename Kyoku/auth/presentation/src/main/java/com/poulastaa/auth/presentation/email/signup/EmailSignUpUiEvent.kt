package com.poulastaa.auth.presentation.email.signup

import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.designsystem.UiText

sealed interface EmailSignUpUiEvent {
    data class EmitToast(val message: UiText) : EmailSignUpUiEvent
    data class OnSuccessNavigate(val screen: SavedScreen) : EmailSignUpUiEvent

    data object NavigateToLogIn : EmailSignUpUiEvent
}