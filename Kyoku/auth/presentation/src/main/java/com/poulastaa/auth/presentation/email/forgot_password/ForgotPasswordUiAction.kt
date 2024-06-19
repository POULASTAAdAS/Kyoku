package com.poulastaa.auth.presentation.email.forgot_password

import com.poulastaa.core.presentation.ui.UiText

sealed interface ForgotPasswordUiAction {
    data object NavigateBack : ForgotPasswordUiAction
    data class EmitToast(val message: UiText) : ForgotPasswordUiAction
}