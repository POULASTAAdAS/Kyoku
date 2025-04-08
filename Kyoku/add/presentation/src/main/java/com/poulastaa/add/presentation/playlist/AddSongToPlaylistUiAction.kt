package com.poulastaa.add.presentation.playlist

import com.poulastaa.core.domain.model.SongId

internal sealed interface AddSongToPlaylistUiAction {
    data class OnAddSongClick(
        val songId: SongId,
        val type: AddToPlaylistItemUiType,
    ) : AddSongToPlaylistUiAction
}