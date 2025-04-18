package com.poulastaa.add.presentation.playlist.artist

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface AddSongTOPlaylistArtistUiEvent {
    data class EmitToast(val message: UiText) : AddSongTOPlaylistArtistUiEvent
    data class NavigateToAlbum(val albumId: AlbumId) : AddSongTOPlaylistArtistUiEvent
}