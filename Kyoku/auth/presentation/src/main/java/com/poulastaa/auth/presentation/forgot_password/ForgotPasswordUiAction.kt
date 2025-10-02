package com.poulastaa.auth.presentation.forgot_password

sealed interface ForgotPasswordUiAction {
    data class OnEmailChange(val email: String) : ForgotPasswordUiAction

    data object OnNavigateBack : ForgotPasswordUiAction

    data object OnSummit : ForgotPasswordUiAction
    data object OnVerifyClick : ForgotPasswordUiAction
}