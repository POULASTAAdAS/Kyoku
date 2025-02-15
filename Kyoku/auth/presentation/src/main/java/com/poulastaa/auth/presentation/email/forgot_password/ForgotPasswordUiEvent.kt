package com.poulastaa.auth.presentation.email.forgot_password

import com.poulastaa.core.presentation.designsystem.UiText

sealed interface ForgotPasswordUiEvent {
    data class EmitToast(val message: UiText) : ForgotPasswordUiEvent
}