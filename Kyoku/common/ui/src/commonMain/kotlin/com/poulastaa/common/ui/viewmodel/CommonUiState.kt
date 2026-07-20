package com.poulastaa.common.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CommonUiState(
    val snackbarData: SnackbarData? = null,
)

data class SnackbarData(
    val message: String,
    val type: SnackbarType,
)

enum class SnackbarType {
    Error,
    Success,
    Info,
}

@Stable
@Immutable
object CommonUiStateHolder {
    private val _uiState = MutableStateFlow(CommonUiState())
    val uiState = _uiState.asStateFlow()

    internal fun showSnackbar(
        message: String,
        type: SnackbarType,
    ) {
        _uiState.update { it.copy(snackbarData = SnackbarData(message, type)) }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(snackbarData = null) }
    }
}
