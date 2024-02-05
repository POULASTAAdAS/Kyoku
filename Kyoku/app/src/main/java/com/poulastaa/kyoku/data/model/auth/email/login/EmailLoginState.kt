package com.poulastaa.kyoku.data.model.auth.email.login

data class EmailLoginState(
    val email: String = "",
    val password: String = "",

    val emailSupportingText: String = "",
    val passwordSupportingText: String = "",

    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,

    val isLoading: Boolean = false,

    val isPasswordVisible: Boolean = true,

    val isInternetAvailable: Boolean = false
)
