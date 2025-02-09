package com.poulastaa.auth.presentation.email.signup

sealed interface EmailSignUpUiAction {
    data class OnUsernameChange(val username: String) : EmailSignUpUiAction
    data class OnEmailChange(val email: String) : EmailSignUpUiAction
    data class OnPasswordChange(val password: String) : EmailSignUpUiAction
    data class OnConformPasswordChange(val password: String) : EmailSignUpUiAction

    data object OnPasswordVisibilityToggle : EmailSignUpUiAction

    data object OnEmailLogInClick : EmailSignUpUiAction
    data class OnConformClick(val countryCode: String) : EmailSignUpUiAction

    data object OnCreatePasskey : EmailSignUpUiAction
    data object OnCancelPasskeyCreation : EmailSignUpUiAction
}