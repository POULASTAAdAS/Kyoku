package com.poulastaa.auth.presentation.intro

import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.ui.UiText

sealed interface IntroUiEvent {
    data class EmitToast(val message: UiText) : IntroUiEvent
    data class OnSuccess(val screen: SavedScreen) : IntroUiEvent
}