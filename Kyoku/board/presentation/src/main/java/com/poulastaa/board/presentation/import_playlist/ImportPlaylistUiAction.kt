package com.poulastaa.board.presentation.import_playlist

import com.poulastaa.core.domain.utils.PlaylistId

internal sealed interface ImportPlaylistUiAction {
    data class OnLinkChange(val link: String) : ImportPlaylistUiAction
    data object OnImportClick : ImportPlaylistUiAction

    data class OnItemStateToggle(val id: PlaylistId) : ImportPlaylistUiAction

    data object OnSkipClick : ImportPlaylistUiAction
}