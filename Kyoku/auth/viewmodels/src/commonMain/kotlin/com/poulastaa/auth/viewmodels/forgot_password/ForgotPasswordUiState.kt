package com.poulastaa.auth.viewmodels.forgot_password

import com.poulastaa.common.viewmodels.states.UiTextFiledState

data class ForgotPasswordUiState(
    val isMakingApiCall: Boolean = false,
    val email: UiTextFiledState = UiTextFiledState(),
)
