package com.poulastaa.play.presentation.view_artist

sealed interface ViewArtistOtherScreen {
    data class ViewArtist(val id: Long) : ViewArtistOtherScreen
    data class AddSongToPlaylist(val id: Long) : ViewArtistOtherScreen
}