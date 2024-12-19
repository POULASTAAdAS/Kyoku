package com.poulastaa.auth.presentation.email.login

sealed interface EmailLogInUiAction {
    data class OnEmailChange(val email: String) : EmailLogInUiAction
    data class OnPasswordChange(val password: String) : EmailLogInUiAction

    data object OnPasswordVisibilityToggle : EmailLogInUiAction

    data object OnEmailSignUpClick : EmailLogInUiAction
    data object OnConformClick : EmailLogInUiAction

    data object OnForgotPasswordClick : EmailLogInUiAction
}