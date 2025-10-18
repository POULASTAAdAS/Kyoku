package com.poulastaa.auth.presentation.intro

import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password


sealed interface IntroUiAction {
    data class OnEmailChange(val email: Email) : IntroUiAction
    data class OnPasswordChange(val password: Password) : IntroUiAction
    data object ObPasswordVisibilityToggle : IntroUiAction
    data object OnForgotPasswordClick : IntroUiAction
    data object OnEmailSubmit : IntroUiAction

    data object OnEmailSingUpClick : IntroUiAction

    data object OnGoogleAuthClick : IntroUiAction
}