package com.poulastaa.auth.viewmodels.reset_password

sealed interface ResetPasswordUiAction {
    data class OnTokenReceived(val token: String) : ResetPasswordUiAction
    data class OnPasswordChange(val password: String) : ResetPasswordUiAction
    data class OnConfirmPasswordChange(val password: String) : ResetPasswordUiAction

    data object OnPasswordVisibilityToggle : ResetPasswordUiAction
    data object OnConfirmPasswordVisibilityToggle : ResetPasswordUiAction

    data object ResetPassword : ResetPasswordUiAction
}
