package com.poulastaa.setup.viewmodels.import_playlist

sealed interface ImportPlaylistUiAction {
    data class OnLinkChange(val link: String) : ImportPlaylistUiAction
    data object OnImportClick : ImportPlaylistUiAction
    data class OnPlaylistToggle(val playlistId: Long) : ImportPlaylistUiAction
}
