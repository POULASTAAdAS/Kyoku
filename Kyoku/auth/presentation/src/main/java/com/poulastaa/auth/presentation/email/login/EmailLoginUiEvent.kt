package com.poulastaa.auth.presentation.email.login

sealed interface EmailLoginUiEvent {
    data class OnEmailChange(val value: String) : EmailLoginUiEvent
    data class OnPasswordChange(val value: String) : EmailLoginUiEvent

    data object OnForgotPasswordClick : EmailLoginUiEvent
    data object OnEmailSignUpClick : EmailLoginUiEvent

    data object OnContinueClick : EmailLoginUiEvent
    data object OnResendMailClick : EmailLoginUiEvent
    data object OnPasswordVisibilityToggle : EmailLoginUiEvent
}