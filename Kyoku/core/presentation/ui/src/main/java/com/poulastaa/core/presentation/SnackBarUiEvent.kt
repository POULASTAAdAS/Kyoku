package com.poulastaa.core.presentation

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.designsystem.UiText

@Stable
data class SnackBarUiEvent(
    val eventType: SnackBarEventType,
    val message: UiText = UiText.DynamicString(""),
)
