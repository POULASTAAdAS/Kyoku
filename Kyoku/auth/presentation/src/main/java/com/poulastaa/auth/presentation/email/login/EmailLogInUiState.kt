package com.poulastaa.auth.presentation.email.login

import com.poulastaa.core.presentation.ui.model.TextHolder

data class EmailLogInUiState(
    val isMakingApiCall: Boolean = false,

    val isValidEmail: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val email: TextHolder = TextHolder(),
    val password: TextHolder = TextHolder(),

    val isResendVerificationMailVisible: Boolean = false,
    val canResendMailAgain: Boolean = false,
)
