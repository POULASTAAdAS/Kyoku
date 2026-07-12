package com.poulastaa.auth.ui.forgot_password

sealed interface ForgotPasswordUiEvent {
    data class NavigateToOtp(val email: String) : ForgotPasswordUiEvent
}
