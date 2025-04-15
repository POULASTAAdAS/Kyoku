package com.poulastaa.add.presentation.playlist

import com.poulastaa.core.domain.model.SongId

internal sealed interface AddSongToPlaylistUiAction {
    enum class PageType {
        YOUR_FAVOURITE,
        SUGGESTED_FOR_YOU,
        YOU_MAY_ALSO_LIKE,
        SEARCH
    }

    data class OnItemClick(
        val itemId: SongId,
        val type: AddToPlaylistItemUiType,
        val pageType: PageType,
    ) : AddSongToPlaylistUiAction

    data class OnSearchQueryChange(val value: String) : AddSongToPlaylistUiAction
    data class OnSearchFilterTypeChange(
        val type: AddSongToPlaylistSearchUiFilterType,
    ) : AddSongToPlaylistUiAction

    data class OnOtherScreenClose(val type: AddToPlaylistItemUiType) : AddSongToPlaylistUiAction
}