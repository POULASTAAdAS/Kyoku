package com.poulastaa.kyoku.data.model.auth.email.forgot_password

data class ForgotPasswordState(
    val email: String = "",
    val getEmailClicked: Boolean = false,

    val isEmailError: Boolean = false,
    val emailSupportingText: String = "",

    val navigateBackClicked: Boolean = false,
    val isInternetAvailable: Boolean = false,

    val isLoading: Boolean = false
)
