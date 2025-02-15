package com.poulastaa.core.presentation.designsystem.model

import com.poulastaa.core.presentation.designsystem.UiText

data class TextHolder(
    val value: String = "",
    val isValid: Boolean = false,
    val isErr: Boolean = false,
    val errText: UiText = UiText.DynamicString(""),
)
