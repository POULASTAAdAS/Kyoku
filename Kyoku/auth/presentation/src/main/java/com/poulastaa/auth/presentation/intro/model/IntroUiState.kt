package com.poulastaa.auth.presentation.intro.model

internal data class IntroUiState(
    val isLoading: Boolean = false,

    val email: EmailTextProp = EmailTextProp(),
    val password: PasswordTextProp = PasswordTextProp(),
    val isNewEmailUser: Boolean = false,
)
