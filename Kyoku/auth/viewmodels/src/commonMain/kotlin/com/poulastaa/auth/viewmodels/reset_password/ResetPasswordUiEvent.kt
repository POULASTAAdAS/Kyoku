package com.poulastaa.auth.viewmodels.reset_password

sealed interface ResetPasswordUiEvent {
    data object NavigateToSignIn : ResetPasswordUiEvent
    data object NavigateToForgotPassword : ResetPasswordUiEvent
}
