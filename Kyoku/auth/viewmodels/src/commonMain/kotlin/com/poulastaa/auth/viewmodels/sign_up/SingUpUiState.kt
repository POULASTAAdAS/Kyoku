package com.poulastaa.auth.viewmodels.sign_up

import com.poulastaa.common.viewmodels.states.UiTextFiledState

data class SingUpUiState(
    val isMakingApiCall: Boolean = false,
    val isGoogleAuthInProgress: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val email: UiTextFiledState = UiTextFiledState(),
    val password: UiTextFiledState = UiTextFiledState(),
    val username: UiTextFiledState = UiTextFiledState(),
)
