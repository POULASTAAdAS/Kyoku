package com.poulastaa.auth.viewmodels.sign_in

import com.poulastaa.common.viewmodels.states.UiTextFiledState

data class SignInUiState(
    val isGoogleAuthInProgress: Boolean = false,
    val isMakingApiCall: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val email: UiTextFiledState = UiTextFiledState(),
    val password: UiTextFiledState = UiTextFiledState(),
)
