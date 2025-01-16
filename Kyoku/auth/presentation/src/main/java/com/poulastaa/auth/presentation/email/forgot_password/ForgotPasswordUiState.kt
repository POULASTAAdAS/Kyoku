package com.poulastaa.auth.presentation.email.forgot_password

import com.poulastaa.core.presentation.ui.model.TextHolder

data class ForgotPasswordUiState(
    val isMakingApiCall: Boolean = false,
    val email: TextHolder = TextHolder(),
)
