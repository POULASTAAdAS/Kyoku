package com.poulastaa.play.presentation.view_artist

sealed interface ViewArtistUiEvent {
    data class OnSongClick(val id: Long) : ViewArtistUiEvent
    data object ExploreArtistOpenClick : ViewArtistUiEvent
    data object ExploreArtistCloseClick : ViewArtistUiEvent

    data object FollowArtistToggleClick : ViewArtistUiEvent
}