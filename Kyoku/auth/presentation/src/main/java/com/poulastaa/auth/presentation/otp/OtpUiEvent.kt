package com.poulastaa.auth.presentation.otp

import com.poulastaa.core.domain.JWTToken
import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface OtpUiEvent {
    data class EmitToast(val message: UiText) : OtpUiEvent
    data object NavigateBack : OtpUiEvent
    data class NavigateToResetPassword(val token: JWTToken) : OtpUiEvent
}