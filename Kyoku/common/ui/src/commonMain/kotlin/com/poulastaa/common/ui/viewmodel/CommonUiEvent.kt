package com.poulastaa.common.ui.viewmodel

sealed interface CommonUiEvent {
    data class ShowError(val message: String) : CommonUiEvent
}
