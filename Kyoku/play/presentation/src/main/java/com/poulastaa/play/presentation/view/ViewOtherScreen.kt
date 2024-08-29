package com.poulastaa.play.presentation.view

sealed interface ViewOtherScreen {
    data class ViewSongArtists(val id: Long) : ViewOtherScreen
    data class AddSongToPlaylist(val id: Long) : ViewOtherScreen
}