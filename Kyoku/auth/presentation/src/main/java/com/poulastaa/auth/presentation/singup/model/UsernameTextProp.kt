package com.poulastaa.auth.presentation.singup.model

import com.poulastaa.core.presentation.designsystem.TextProp

internal data class UsernameTextProp(
    val isValidUsername: Boolean = false,
    val prop: TextProp = TextProp(),
)
