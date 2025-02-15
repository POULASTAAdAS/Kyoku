package com.poulastaa.setup.presentation.set_bdate

import com.poulastaa.core.presentation.designsystem.UiText

sealed interface SetBDateUiEvent {
    data class EmitToast(val message: UiText) : SetBDateUiEvent
    data object NavigateBack : SetBDateUiEvent
    data object OnSuccess : SetBDateUiEvent
}