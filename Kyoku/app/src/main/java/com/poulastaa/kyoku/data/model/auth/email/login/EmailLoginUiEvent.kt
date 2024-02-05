package com.poulastaa.kyoku.data.model.auth.email.login

sealed class EmailLoginUiEvent {
    data class OnEmailEnter(val data: String) : EmailLoginUiEvent()
    data class OnPasswordEnter(val data: String) : EmailLoginUiEvent()

    data class OnAutoFillEmail(val email: String) : EmailLoginUiEvent()
    data class OnAutoFillPassword(val password: String) : EmailLoginUiEvent()

    data object OnContinueClick : EmailLoginUiEvent()

    data object OnPasswordVisibilityChange : EmailLoginUiEvent()

    data object OnForgotPasswordClick : EmailLoginUiEvent()

    data object OnSignUpClick : EmailLoginUiEvent()
}