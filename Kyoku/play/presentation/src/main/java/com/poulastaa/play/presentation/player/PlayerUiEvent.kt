package com.poulastaa.play.presentation.player

sealed interface PlayerUiEvent {
    data object OnPlayerExtendClick : PlayerUiEvent
    data object OnPlayerShrinkClick : PlayerUiEvent

    sealed interface PlayBackController : PlayerUiEvent {
        data class OnPlayPause(val songId: Long) : PlayBackController
        data class SeekTo(val pos: Float) : PlayBackController
        data object OnPlayNextClick : PlayBackController
        data object OnPlayPrevClick : PlayBackController
        data class OnSongClick(val songId: Long) : PlayBackController
    }

    data object ClosePlayer : PlayerUiEvent
}