package com.poulastaa.auth.presentation.otp

import androidx.compose.runtime.Stable
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.presentation.designsystem.TextProp

@Stable
enum class ResendState {
    IDEAL,
    TICKER,
    ENABLED,
}

@Stable
enum class ReturnState {
    IDEAL,
    WARNING,
    CONFIRM
}

@Stable
internal data class OtpUiState(
    val isLoading: Boolean = false,

    val returnState: ReturnState = ReturnState.IDEAL,

    val isTryAnotherEmailVisible: Boolean = false,
    val resendState: ResendState = ResendState.IDEAL,

    val email: Email = "",
    val otp: TextProp = TextProp(),
    val ticker: String = "02:30",
)
