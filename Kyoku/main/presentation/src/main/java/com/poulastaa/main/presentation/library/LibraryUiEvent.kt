package com.poulastaa.main.presentation.library

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface LibraryUiEvent {
    data class EmitToast(val message: UiText) : LibraryUiEvent
}