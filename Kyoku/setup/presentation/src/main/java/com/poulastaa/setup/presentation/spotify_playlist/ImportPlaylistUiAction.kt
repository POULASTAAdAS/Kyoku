package com.poulastaa.setup.presentation.spotify_playlist

sealed interface ImportPlaylistUiAction {
    data class OnLinkChange(val link: String) : ImportPlaylistUiAction
    data class OnPlaylistClick(val playlistId: Long) : ImportPlaylistUiAction
    data object OnImportClick : ImportPlaylistUiAction
    data object OnSkipClick : ImportPlaylistUiAction
}