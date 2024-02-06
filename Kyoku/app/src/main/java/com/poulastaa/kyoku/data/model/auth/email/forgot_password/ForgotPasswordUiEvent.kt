package com.poulastaa.kyoku.data.model.auth.email.forgot_password

sealed class ForgotPasswordUiEvent {
    data class OnEmailEnter(val data: String) : ForgotPasswordUiEvent()
    data class OnAutoFillEmailClick(val email: String) : ForgotPasswordUiEvent()

    data object OnGetEmailClick : ForgotPasswordUiEvent()
    data object OnNavigateBackClicked : ForgotPasswordUiEvent()
}