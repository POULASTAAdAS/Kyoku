package com.poulastaa.play.presentation.create_playlist

import com.poulastaa.core.domain.model.CreatePlaylistType

sealed interface CreatePlaylistUiEvent {
    data object OnSearchToggle : CreatePlaylistUiEvent
    data class OnSearchQueryChange(val value: String) : CreatePlaylistUiEvent

    data class OnSongClick(val type: CreatePlaylistType, val songId: Long) : CreatePlaylistUiEvent
}