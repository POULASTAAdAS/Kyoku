package com.poulastaa.play.presentation.player

import com.poulastaa.core.domain.PlayType

sealed interface PlayerUiEvent {
    sealed interface PlayOperation : PlayerUiEvent {
        data class PlayAll(val id: Long, val type: PlayType) : PlayOperation
        data class ShuffleAll(val id: Long, val type: PlayType) : PlayOperation
    }

    data object OnPlayerExtendClick : PlayerUiEvent
    data object OnPlayerShrinkClick : PlayerUiEvent

    sealed interface PlayBackController : PlayerUiEvent {
        data class OnPlayPause(val songId: Long) : PlayBackController
        data class SeekTo(val pos: Float) : PlayBackController
        data object OnPlayNextClick : PlayBackController
        data object OnPlayPrevClick : PlayBackController
        data class OnQueueSongClick(val songId: Long) : PlayBackController
    }

    data class GetSongInfo(val songId: Long) : PlayerUiEvent
    data class OnArtistClick(val artistId: Long) : PlayerUiEvent
    data object ClosePlayer : PlayerUiEvent
}