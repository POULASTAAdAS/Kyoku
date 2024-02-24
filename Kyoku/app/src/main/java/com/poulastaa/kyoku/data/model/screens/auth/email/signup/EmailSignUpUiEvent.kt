package com.poulastaa.kyoku.data.model.screens.auth.email.signup

import android.app.Activity

sealed class EmailSignUpUiEvent {
    data class OnEmailEnter(val data: String) : EmailSignUpUiEvent()
    data class OnUsernameEnter(val data: String) : EmailSignUpUiEvent()
    data class OnPasswordEnter(val data: String) : EmailSignUpUiEvent()
    data class OnConformPasswordEnter(val data: String) : EmailSignUpUiEvent()

    data object OnResendVerificationMailClick : EmailSignUpUiEvent()

    data class OnAutoFillEmail(val email: String) : EmailSignUpUiEvent()
    data class OnAutoFillPassword(val password: String) : EmailSignUpUiEvent()
    data class OnAutoFillUserName(val username: String) : EmailSignUpUiEvent()

    data object OnPasswordVisibilityChange : EmailSignUpUiEvent()

    data class OnContinueClick(val activity: Activity) : EmailSignUpUiEvent()

    data object SomeErrorOccurredOnAuth : EmailSignUpUiEvent()

    data class EmitEmailSupportingText(val message: String) : EmailSignUpUiEvent()

    data object OnAuthCanceled : EmailSignUpUiEvent()

    data class EmitToast(val message: String) : EmailSignUpUiEvent()

    data object OnLogInClick : EmailSignUpUiEvent()
}