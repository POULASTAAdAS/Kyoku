package com.poulastaa.auth.presentation.model

import com.poulastaa.core.presentation.designsystem.TextProp

data class PasswordTextProp(
    val isPasswordVisible: Boolean = false,
    val prop: TextProp = TextProp(),
)