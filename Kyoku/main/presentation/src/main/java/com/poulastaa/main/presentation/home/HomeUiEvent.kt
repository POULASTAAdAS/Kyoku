package com.poulastaa.main.presentation.home

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface HomeUiEvent {
    data class EmitToast(val message: UiText) : HomeUiEvent
}