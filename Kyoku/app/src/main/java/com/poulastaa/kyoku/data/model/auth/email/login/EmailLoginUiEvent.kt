package com.poulastaa.kyoku.data.model.auth.email.login

sealed class EmailLoginUiEvent {
    data class OnEmailEnter(val data: String) : EmailLoginUiEvent()
    data class OnPasswordEnter(val data: String) : EmailLoginUiEvent()

    data class OnAutoFillEmail(val email: String) : EmailLoginUiEvent()
    data class OnAutoFillPassword(val password: String) : EmailLoginUiEvent()

    data object OnContinueClick : EmailLoginUiEvent()

    data object OnPasswordVisibilityChange : EmailLoginUiEvent()

    data object OnForgotPasswordClick : EmailLoginUiEvent()

    data class EmitEmailSupportingText(val message: String) : EmailLoginUiEvent()
    data class EmitPasswordSupportingText(val message: String) : EmailLoginUiEvent()

    data object OnAuthCanceled: EmailLoginUiEvent()
    data object SomeErrorOccurredOnAuth : EmailLoginUiEvent()

    data object OnResendVerificationMailClick : EmailLoginUiEvent()

    data class EmitToast(val message: String): EmailLoginUiEvent()

    data object OnSignUpClick : EmailLoginUiEvent()
}