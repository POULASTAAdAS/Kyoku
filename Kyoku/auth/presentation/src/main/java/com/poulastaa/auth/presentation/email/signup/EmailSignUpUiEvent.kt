package com.poulastaa.auth.presentation.email.signup

sealed interface EmailSignUpUiEvent {
    data class OnUserNameChange(val value: String) : EmailSignUpUiEvent
    data class OnEmailChange(val value: String) : EmailSignUpUiEvent
    data class OnPasswordChange(val value: String) : EmailSignUpUiEvent
    data class OnConfirmPasswordChange(val value: String) : EmailSignUpUiEvent

    data object OnPasswordVisibilityToggle : EmailSignUpUiEvent
    data object OnEmailLogInClick : EmailSignUpUiEvent

    data object OnContinueClick : EmailSignUpUiEvent
}