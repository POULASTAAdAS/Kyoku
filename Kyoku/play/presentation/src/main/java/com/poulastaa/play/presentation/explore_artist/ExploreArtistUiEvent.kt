package com.poulastaa.play.presentation.explore_artist

sealed interface ExploreArtistUiEvent {
    data class OnSongClick(val id: Long) : ExploreArtistUiEvent
    data class OnSongThreeDotClick(val id: Long) : ExploreArtistUiEvent

    data class OnAlbumClick(val id: Long) : ExploreArtistUiEvent
    data class OnAlbumThreeDotClick(val id: Long) : ExploreArtistUiEvent

    data object OnFollowToggle : ExploreArtistUiEvent
}