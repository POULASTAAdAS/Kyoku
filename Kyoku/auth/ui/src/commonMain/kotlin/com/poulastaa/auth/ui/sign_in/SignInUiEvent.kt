package com.poulastaa.auth.ui.sign_in

sealed interface SignInUiEvent {
    data class NavigateToForgotPassword(val email: String?) : SignInUiEvent
    data object NavigateToSignUp : SignInUiEvent
    data object StartGoogleAuthFlow : SignInUiEvent
    data object NavigateToImportPlaylist : SignInUiEvent
    data object NavigateToHome : SignInUiEvent
}
