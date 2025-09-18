package com.poulastaa.auth.presentation.intro.components

import com.poulastaa.core.presentation.designsystem.TextProp

data class EmailTextProp(
    val isValidEmail: Boolean = false,
    val prop: TextProp = TextProp(),
)
