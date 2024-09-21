package com.poulastaa.play.presentation.view_artist

import com.poulastaa.play.domain.ViewSongOperation

sealed interface ViewArtistUiEvent {
    data class OnSongClick(val id: Long) : ViewArtistUiEvent

    sealed interface ThreeDotEvent : ViewArtistUiEvent {
        data class OnClick(val id: Long) : ThreeDotEvent
        data class OnCloseClick(val id: Long) : ThreeDotEvent
        data class OnThreeDotItemClick(
            val id: Long,
            val operation: ViewSongOperation,
        ) : ThreeDotEvent
    }

    data object FollowArtistToggleClick : ViewArtistUiEvent
}