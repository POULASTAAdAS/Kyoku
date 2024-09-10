package com.poulastaa.play.presentation.create_playlist.album

sealed interface CreatePlaylistAlbumUiEvent {
    data class OnSongClick(val songId: Long) : CreatePlaylistAlbumUiEvent
}