package com.poulastaa.auth.ui.sign_in

sealed interface SignInUiAction {
    data class OnEmailChange(val email: String) : SignInUiAction
    data class OnPasswordChange(val password: String) : SignInUiAction

    data object SignIn : SignInUiAction

    data class OnForgotPasswordClick(val email: String) : SignInUiAction
    data class OnGoogleAuthSuccess(val token: String) : SignInUiAction

    data object OnCreateAccountClick : SignInUiAction
    data object OnGoogleSignInClick : SignInUiAction
    data object OnGoogleAuthCanceled : SignInUiAction
    data object OnPasswordVisibilityToggle : SignInUiAction
}
