package com.poulastaa.common.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CommonUiState(
    val snackbarData: SnackbarData? = null,
)

data class SnackbarData(
    val message: String,
    val type: SnackbarType,
    val action: SnackbarAction? = null,
)

/**
 * Optional clickable text shown in the snackbar; [onClick] runs, then the snackbar is dismissed.
 */
data class SnackbarAction(
    val label: String,
    val onClick: () -> Unit,
)

enum class SnackbarType {
    Error,
    Success,
    Info,
}

object CommonUiStateHolder {
    private val _uiState = MutableStateFlow(CommonUiState())
    val uiState = _uiState.asStateFlow()

    internal fun showSnackbar(
        message: String,
        type: SnackbarType,
        action: SnackbarAction? = null,
    ) {
        _uiState.update { it.copy(snackbarData = SnackbarData(message, type, action)) }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(snackbarData = null) }
    }
}
