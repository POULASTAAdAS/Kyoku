package com.poulastaa.auth.presentation.email.forgot_password

import com.poulastaa.core.presentation.ui.UiText

sealed interface ForgotPasswordUiEvent {
    data class EmitToast(val message: UiText) : ForgotPasswordUiEvent
}