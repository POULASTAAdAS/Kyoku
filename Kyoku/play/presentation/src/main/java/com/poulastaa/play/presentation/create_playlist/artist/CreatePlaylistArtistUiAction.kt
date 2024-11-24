package com.poulastaa.play.presentation.create_playlist.artist

sealed interface CreatePlaylistArtistUiAction {
    data class NavigateToAlbum(val albumId: Long) : CreatePlaylistArtistUiAction
}