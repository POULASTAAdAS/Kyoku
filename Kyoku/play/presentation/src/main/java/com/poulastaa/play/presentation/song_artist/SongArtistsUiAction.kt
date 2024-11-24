package com.poulastaa.play.presentation.song_artist

import com.poulastaa.core.presentation.ui.UiText

sealed interface SongArtistsUiAction {
    data class EmitToast(val message: UiText) : SongArtistsUiAction
    data class NavigateToArtist(val artistId: Long) : SongArtistsUiAction
}