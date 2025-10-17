package com.poulastaa.auth.presentation.forgot_password

import androidx.compose.runtime.Stable
import com.poulastaa.auth.presentation.intro.model.EmailTextProp

@Stable
data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val isVerifyButtonEnabled: Boolean = false,

    val ticker: String = "01:00",
    val isTickerVisible: Boolean = false,
    val email: EmailTextProp = EmailTextProp(),
)
