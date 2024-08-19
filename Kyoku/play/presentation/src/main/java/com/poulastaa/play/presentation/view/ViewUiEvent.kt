package com.poulastaa.play.presentation.view

sealed interface ViewUiEvent {
    data object OnPlayClick : ViewUiEvent
    data object OnDownloadClick : ViewUiEvent
    data object OnShuffleClick : ViewUiEvent

    data class OnMoveClick(val songId: Long) : ViewUiEvent
    data class OnSongClick(val songId: Long) : ViewUiEvent
    data class OnThreeDotClick(val songId: Long) : ViewUiEvent
}