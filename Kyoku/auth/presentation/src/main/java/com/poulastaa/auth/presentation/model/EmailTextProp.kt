package com.poulastaa.auth.presentation.model

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.designsystem.TextProp

@Stable
data class EmailTextProp(
    val isValidEmail: Boolean = false,
    val prop: TextProp = TextProp(),
)