package com.poulastaa.auth.presentation.email.signup

import com.poulastaa.core.presentation.ui.model.TextHolder

data class EmailSignUpUiState(
    val isMakingApiCall: Boolean = false,

    val isValidUserName:Boolean = false,
    val userName: TextHolder = TextHolder(),

    val isValidEmail: Boolean = false,
    val email: TextHolder = TextHolder(),

    val isPasswordVisible: Boolean = false,
    val password: TextHolder = TextHolder(),
    val confirmPassword: TextHolder = TextHolder(),
)
