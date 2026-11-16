package com.poulastaa.auth.viewmodels.forgot_password

sealed interface ForgotPasswordUiAction {
    data class OnEmailChange(val email: String) : ForgotPasswordUiAction

    data object GetOtp : ForgotPasswordUiAction
}
