package com.poulastaa.auth.presentation.email.signup

import com.poulastaa.core.presentation.ui.model.TextHolder

data class EmailSignUpUiState(
    val isMakingApiCall: Boolean = false,

    val email: TextHolder = TextHolder(),
    val password: TextHolder = TextHolder(),
    val username: TextHolder = TextHolder(),
    val conformPassword: TextHolder = TextHolder(),

    val isPasswordVisible: Boolean = false,

    val isPasskeyCreatePopUp: Boolean = false,
)
