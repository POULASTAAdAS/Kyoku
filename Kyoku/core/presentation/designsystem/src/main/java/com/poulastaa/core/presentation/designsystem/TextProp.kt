package com.poulastaa.core.presentation.designsystem

data class TextProp(
    val value: String = "",
    val isErr: Boolean = false,
    val errText: UiText = UiText.DynamicString(""),
)