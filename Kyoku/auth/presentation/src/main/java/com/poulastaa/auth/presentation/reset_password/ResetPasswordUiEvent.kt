package com.poulastaa.auth.presentation.reset_password

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface ResetPasswordUiEvent {
    data class EmitToast(val message: UiText) : ResetPasswordUiEvent
    data object TriggerAnimation : ResetPasswordUiEvent
    data object NavigateBack : ResetPasswordUiEvent
    data object PopUpToIntroScreen : ResetPasswordUiEvent
}