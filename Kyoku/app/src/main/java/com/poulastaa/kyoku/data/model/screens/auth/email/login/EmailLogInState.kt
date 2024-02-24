package com.poulastaa.kyoku.data.model.screens.auth.email.login

data class EmailLogInState(
    val email: String = "",
    val password: String = "",

    val emailSupportingText: String = "",
    val passwordSupportingText: String = "",

    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,

    val isLoading: Boolean = false,

    val isResendVerificationMailPromptVisible: Boolean = false,
    val sendVerificationMailTimer: Int = 20,
    val isResendMailEnabled: Boolean = false,

    val isPasswordVisible: Boolean = true,

    val isInternetAvailable: Boolean = false
)
