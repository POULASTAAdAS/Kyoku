package com.poulastaa.auth.presentation.email.login

import com.poulastaa.core.presentation.ui.model.TextHolder

data class EmailLoginUiState(
    val isMakingApiCall: Boolean = false,

    val email: TextHolder = TextHolder(),
    val password: TextHolder = TextHolder(),
    val isPasswordVisible: Boolean = false,
)
