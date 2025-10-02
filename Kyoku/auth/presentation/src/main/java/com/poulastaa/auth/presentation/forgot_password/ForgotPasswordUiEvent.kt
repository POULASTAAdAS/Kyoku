package com.poulastaa.auth.presentation.forgot_password

import com.poulastaa.auth.presentation.forgot_password.model.ForgotPasswordAllowedNavigationScreens
import com.poulastaa.core.presentation.designsystem.UiText

sealed interface ForgotPasswordUiEvent {
    data class EmitToast(val message: UiText) : ForgotPasswordUiEvent
    data class OnNavigate(
        val screen: ForgotPasswordAllowedNavigationScreens,
    ) : ForgotPasswordUiEvent
}