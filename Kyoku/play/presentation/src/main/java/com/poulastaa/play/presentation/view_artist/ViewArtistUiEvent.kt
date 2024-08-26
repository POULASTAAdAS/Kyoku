package com.poulastaa.play.presentation.view_artist

sealed interface ViewArtistUiEvent {
    data class OnSongClick(val id: Long) : ViewArtistUiEvent

    data object FollowArtistToggleClick : ViewArtistUiEvent
}