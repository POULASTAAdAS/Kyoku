package com.poulastaa.setup.presentation.pic_genre

import com.poulastaa.core.presentation.designsystem.UiText

sealed interface PicGenreUiEvent {
    data class EmitToast(val message: UiText) : PicGenreUiEvent
    data object OnSuccess : PicGenreUiEvent
}