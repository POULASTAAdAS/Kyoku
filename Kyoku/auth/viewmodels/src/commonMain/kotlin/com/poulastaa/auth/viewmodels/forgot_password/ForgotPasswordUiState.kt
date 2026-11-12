package com.poulastaa.auth.viewmodels.forgot_password

import com.poulastaa.common.viewmodels.states.UiTextFiledState

data class ForgotPasswordUiState(
    val isMakingApiCall: Boolean = false,
    val email: UiTextFiledState = UiTextFiledState(),
) {
    // Not tied to isMakingApiCall: the button keeps its enabled look and shows a loader instead; the viewmodel ignores repeat clicks.
    val isGetOtpEnabled: Boolean
        get() = email.isError.not()
}
