package com.poulastaa.auth.presentation.email.login

import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.ui.UiText

sealed interface EmailLogInUiEvent {
    data class EmitToast(val message: UiText) : EmailLogInUiEvent
    data class OnSuccess(val screen: SavedScreen) : EmailLogInUiEvent

    data object NavigateToSignUp : EmailLogInUiEvent
    data class NavigateToForgotPassword(val email: String?) : EmailLogInUiEvent
}