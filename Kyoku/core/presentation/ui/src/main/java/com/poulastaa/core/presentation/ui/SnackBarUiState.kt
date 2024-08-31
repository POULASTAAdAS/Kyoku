package com.poulastaa.core.presentation.ui

data class SnackBarUiState(
    val isVisible: Boolean = false,
    val message: UiText = UiText.DynamicString(""),
    val type: SnackBarType = SnackBarType.DEFAULT
)

enum class SnackBarType {
    ERROR,
    SUCCESS,
    DEFAULT
}
