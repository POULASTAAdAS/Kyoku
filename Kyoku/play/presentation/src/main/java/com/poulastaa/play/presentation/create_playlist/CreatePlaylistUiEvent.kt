package com.poulastaa.play.presentation.create_playlist

import com.poulastaa.core.domain.model.CreatePlaylistPagerFilterType
import com.poulastaa.core.domain.model.CreatePlaylistType

sealed interface CreatePlaylistUiEvent {
    data object OnSearchToggle : CreatePlaylistUiEvent
    data class OnSearchQueryChange(val value: String) : CreatePlaylistUiEvent

    data class OnFilterTypeChange(val type: CreatePlaylistPagerFilterType) : CreatePlaylistUiEvent

    data class OnSongClick(val type: CreatePlaylistType, val songId: Long) : CreatePlaylistUiEvent

    data class OnArtistClick(val artistId: Long) : CreatePlaylistUiEvent
    data class OnAlbumClick(val albumId: Long) : CreatePlaylistUiEvent

    data object OnArtistCancel : CreatePlaylistUiEvent
    data object OnAlbumCancel : CreatePlaylistUiEvent
}