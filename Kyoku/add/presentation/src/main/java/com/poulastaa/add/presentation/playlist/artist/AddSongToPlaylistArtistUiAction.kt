package com.poulastaa.add.presentation.playlist.artist

import com.poulastaa.add.presentation.playlist.AddToPlaylistItemUiType

internal sealed interface AddSongToPlaylistArtistUiAction {
    data class OnSearchQueryChange(val value: String) : AddSongToPlaylistArtistUiAction
    data class OnItemClick(
        val itemId: Long,
        val type: AddToPlaylistItemUiType,
    ) : AddSongToPlaylistArtistUiAction

    data class OnSearchFilterTypeChange(
        val type: AddSongToPlaylistArtistSearchFilterType,
    ) : AddSongToPlaylistArtistUiAction

}