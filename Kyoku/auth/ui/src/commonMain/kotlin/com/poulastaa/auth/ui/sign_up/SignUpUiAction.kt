package com.poulastaa.auth.ui.sign_up

sealed interface SignUpUiAction {
    data class OnEmailChange(val email: String) : SignUpUiAction
    data class OnPasswordChange(val password: String) : SignUpUiAction
    data class OnUsernameChange(val username: String) : SignUpUiAction

    data object SignUp: SignUpUiAction

    data class OnGoogleTokenReceived(val token: String) : SignUpUiAction

    data object OnLoginClick : SignUpUiAction
    data object OnGoogleSignInClick : SignUpUiAction
    data object OnGoogleAuthCanceled : SignUpUiAction
    data object OnPasswordVisibilityToggle : SignUpUiAction
}
