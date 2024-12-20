package com.poulastaa.auth.presentation.email.signup

import com.poulastaa.core.presentation.ui.UiText

sealed interface EmailSignUpUiEvent {
    data class EmitToast(val message: UiText) : EmailSignUpUiEvent
    data object OnSuccess : EmailSignUpUiEvent // todo change

    data object NavigateToLogIn : EmailSignUpUiEvent
}