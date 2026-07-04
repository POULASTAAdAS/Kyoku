package com.poulastaa.auth.ui.sign_in

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.poulastaa.common.ui.states.UiTextFiledState

@Stable
@Immutable
data class SignInUiState(
    val isMakingApiCall: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val email: UiTextFiledState = UiTextFiledState(),
    val password: UiTextFiledState = UiTextFiledState(),
)
