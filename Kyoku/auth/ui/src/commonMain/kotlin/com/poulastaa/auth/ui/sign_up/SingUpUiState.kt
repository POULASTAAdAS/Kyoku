package com.poulastaa.auth.ui.sign_up

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.poulastaa.common.ui.states.UiTextFiledState

@Stable
@Immutable
data class SingUpUiState(
    val isMakingApiCall: Boolean = false,
    val email: UiTextFiledState = UiTextFiledState(),
    val password: UiTextFiledState = UiTextFiledState(),
    val username: UiTextFiledState = UiTextFiledState(),
)
