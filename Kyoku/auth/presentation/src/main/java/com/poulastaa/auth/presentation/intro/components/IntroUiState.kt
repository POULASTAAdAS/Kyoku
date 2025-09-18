package com.poulastaa.auth.presentation.intro.components

internal data class IntroUiState(
    val email: EmailTextProp = EmailTextProp(),
    val password: PasswordTextProp = PasswordTextProp(),
    val isNewEmailUser: Boolean = false,
    val isLoading: Boolean = false,
)
