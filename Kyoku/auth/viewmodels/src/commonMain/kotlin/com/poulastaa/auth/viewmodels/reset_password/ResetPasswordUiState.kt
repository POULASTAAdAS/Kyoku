package com.poulastaa.auth.viewmodels.reset_password

import com.poulastaa.common.viewmodels.states.UiTextFiledState

data class ResetPasswordUiState(
    val isMakingApiCall: Boolean = false,
    val token: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val password: UiTextFiledState = UiTextFiledState(),
    val confirmPassword: UiTextFiledState = UiTextFiledState(),
    val isPasswordApiError: Boolean = false,
    val passwordApiErrorMessage: String = "",
) {
    // Not tied to isMakingApiCall: the button keeps its enabled look and shows a loader instead; the viewmodel ignores repeat clicks.
    val isResetEnabled: Boolean
        get() = password.value.isNotEmpty() && confirmPassword.value.isNotEmpty()
}
