package com.poulastaa.auth.presentation.email.forgot_password

sealed interface ForgotPasswordUiEvent {
    data object OnBackClick : ForgotPasswordUiEvent

    data class OnEmailChange(val email: String) : ForgotPasswordUiEvent
    data object OnSendClick : ForgotPasswordUiEvent
}