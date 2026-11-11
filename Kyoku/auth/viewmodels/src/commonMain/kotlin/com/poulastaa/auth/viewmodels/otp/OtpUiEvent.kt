package com.poulastaa.auth.viewmodels.otp

sealed interface OtpUiEvent {
    data class NavigateToResetPassword(val token: String) : OtpUiEvent
}
