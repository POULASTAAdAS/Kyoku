package com.poulastaa.auth.presentation.email.forgot_password

sealed interface ForgotPasswordUiAction {
    data class OnEmailChange(val email: String) : ForgotPasswordUiAction
    data object OnSubmitClick : ForgotPasswordUiAction
}