package com.poulastaa.auth.ui.forgot_password

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.poulastaa.common.ui.states.UiTextFiledState

@Stable
@Immutable
data class ForgotPasswordUiState(
    val isMakingApiCall: Boolean = false,
    val email: UiTextFiledState = UiTextFiledState(),
)
