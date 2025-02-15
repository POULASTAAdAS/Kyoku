package com.poulastaa.setup.presentation.pic_artist

import com.poulastaa.core.presentation.designsystem.UiText

sealed interface PicArtistUiEvent {
    data class EmitToast(val message: UiText) : PicArtistUiEvent
    data object OnSuccess : PicArtistUiEvent
}