package com.poulastaa.add.presentation.playlist

import com.poulastaa.core.domain.model.SongId

internal sealed interface AddSongToPlaylistUiAction {
    data class OnItemClick(
        val itemId: SongId,
        val type: AddToPlaylistItemUiType,
    ) : AddSongToPlaylistUiAction

    data class OnSearchQueryChange(val value: String) : AddSongToPlaylistUiAction
    data class OnSearchFilterTypeChange(val type: AddSongToPlaylistSearchUiFilterType): AddSongToPlaylistUiAction
}