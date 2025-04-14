package com.poulastaa.add.presentation.playlist.album

import com.poulastaa.core.presentation.designsystem.UiText

internal sealed interface AddSongToPlaylistAlbumUiEvent {
    data class EmitToast(val message: UiText) : AddSongToPlaylistAlbumUiEvent
}