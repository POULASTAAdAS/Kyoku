package com.poulastaa.board.presentation.set_bdate

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface SetUpBDateUiEvent {
    data class EmitToast(val message: UiText) : SetUpBDateUiEvent
}