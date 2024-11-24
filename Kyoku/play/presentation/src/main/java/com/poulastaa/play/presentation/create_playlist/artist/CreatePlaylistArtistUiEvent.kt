package com.poulastaa.play.presentation.create_playlist.artist

sealed interface CreatePlaylistArtistUiEvent {
    data class OnAlbumClick(val albumId: Long) : CreatePlaylistArtistUiEvent
    data class OnSongClick(val songId: Long) : CreatePlaylistArtistUiEvent
}