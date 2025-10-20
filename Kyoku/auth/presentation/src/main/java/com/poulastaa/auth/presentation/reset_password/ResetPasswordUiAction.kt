package com.poulastaa.auth.presentation.reset_password

import com.poulastaa.core.domain.utils.Password

internal sealed interface ResetPasswordUiAction {
    data class OnPasswordChange(val password: Password) : ResetPasswordUiAction
    data class OnConformPasswordChange(val password: Password) : ResetPasswordUiAction

    data object OnPasswordVisibilityToggle : ResetPasswordUiAction
    data object OnConformPasswordVisibilityToggle : ResetPasswordUiAction

    data object OnSummit : ResetPasswordUiAction
    data object OnNavigateBack : ResetPasswordUiAction
}