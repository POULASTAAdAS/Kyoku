package com.poulastaa.kyoku.data.model.auth.email.signup

sealed class EmailSignUpUiEvent {
    data class OnEmailEnter(val data: String) : EmailSignUpUiEvent()
    data class OnUsernameEnter(val data: String) : EmailSignUpUiEvent()
    data class OnPasswordEnter(val data: String) : EmailSignUpUiEvent()
    data class OnConformPasswordEnter(val data: String) : EmailSignUpUiEvent()


    data class OnAutoFillEmail(val email: String) : EmailSignUpUiEvent()
    data class OnAutoFillPassword(val password: String) : EmailSignUpUiEvent()
    data class OnAutoFillUserName(val username: String) : EmailSignUpUiEvent()

    data object OnContinueClick : EmailSignUpUiEvent()

    data object OnPasswordVisibilityChange : EmailSignUpUiEvent()

    data object OnLogInClick : EmailSignUpUiEvent()
}