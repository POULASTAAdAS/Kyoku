package com.poulastaa.auth.presentation.email.login

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.ui.UiText

sealed interface EmailLoginUiAction {
    data class EmitToast(val message: UiText) : EmailLoginUiAction
    data class OnSuccess(val route: ScreenEnum) : EmailLoginUiAction

    data object OnEmailSignUp : EmailLoginUiAction
    data class OnForgotPassword(val email: String? = null) : EmailLoginUiAction
}