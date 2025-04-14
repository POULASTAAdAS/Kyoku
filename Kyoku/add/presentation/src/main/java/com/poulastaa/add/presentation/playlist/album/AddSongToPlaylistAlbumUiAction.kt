package com.poulastaa.add.presentation.playlist.album

import com.poulastaa.core.domain.model.SongId

internal sealed interface AddSongToPlaylistAlbumUiAction {
    data class OnItemClick(val songId: SongId) : AddSongToPlaylistAlbumUiAction
}