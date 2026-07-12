package com.poulastaa.auth.ui.forgot_password

sealed interface ForgotPasswordUiAction {
    data class OnEmailChange(val email: String) : ForgotPasswordUiAction

    data class GetOtp(
        val email: String,
    ) : ForgotPasswordUiAction
}
