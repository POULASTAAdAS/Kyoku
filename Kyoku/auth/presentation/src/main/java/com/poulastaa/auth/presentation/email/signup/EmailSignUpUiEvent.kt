package com.poulastaa.auth.presentation.email.signup

import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.ui.UiText

sealed interface EmailSignUpUiEvent {
    data class EmitToast(val message: UiText) : EmailSignUpUiEvent
    data class OnSuccess(val screen: SavedScreen) : EmailSignUpUiEvent

    data object NavigateToLogIn : EmailSignUpUiEvent
}