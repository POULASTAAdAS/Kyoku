package com.poulastaa.auth.presentation.reset_password

import com.poulastaa.auth.presentation.model.PasswordTextProp

internal data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val isSuccessReturnScreenVisible: Boolean = false,

    val password: PasswordTextProp = PasswordTextProp(),
    val conformPassword: PasswordTextProp = PasswordTextProp(),
)
