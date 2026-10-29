package com.poulastaa.auth.viewmodels.forgot_password

sealed interface ForgotPasswordUiEvent {
    data class NavigateToOtp(val email: String) : ForgotPasswordUiEvent
}
