package com.poulastaa.auth.presentation.email.signup

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.ui.UiText

sealed interface EmailSignUpUiAction {
    data class OnSuccess(val screen: ScreenEnum) : EmailSignUpUiAction
    data class EmitToast(val message: UiText) : EmailSignUpUiAction

    data object OnEmailLogIn : EmailSignUpUiAction
}