package com.poulastaa.play.presentation.explore_artist

sealed interface ExploreArtistOtherScreen {
    data class ViewAlbum(val id: Long) : ExploreArtistOtherScreen
    data class AddSongToPlaylist(val id: Long) : ExploreArtistOtherScreen
}