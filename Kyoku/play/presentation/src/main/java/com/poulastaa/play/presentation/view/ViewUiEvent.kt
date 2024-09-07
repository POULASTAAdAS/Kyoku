package com.poulastaa.play.presentation.view

import com.poulastaa.play.domain.ViewSongOperation

sealed interface ViewUiEvent {
    data object OnPlayClick : ViewUiEvent
    data object OnDownloadClick : ViewUiEvent
    data object OnShuffleClick : ViewUiEvent

    data class OnMoveClick(val songId: Long) : ViewUiEvent
    data class OnSongClick(val songId: Long) : ViewUiEvent

    data class OnCreatePlaylistClick(val playlistId: Long) : ViewUiEvent

    data class OnThreeDotClick(val songId: Long) : ViewUiEvent
    data class OnThreeDotClose(val songId: Long) : ViewUiEvent
    data class OnThreeDotItemClick(
        val id: Long,
        val operation: ViewSongOperation
    ) : ViewUiEvent
}
