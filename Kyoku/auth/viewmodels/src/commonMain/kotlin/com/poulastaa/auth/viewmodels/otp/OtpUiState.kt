package com.poulastaa.auth.viewmodels.otp

import com.poulastaa.common.viewmodels.states.UiTextFiledState

data class OtpUiState(
    val isMakingApiCall: Boolean = false,
    val email: String = "",
    val otp: UiTextFiledState = UiTextFiledState(),
)
