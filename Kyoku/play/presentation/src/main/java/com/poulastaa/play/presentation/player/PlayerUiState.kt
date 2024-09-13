package com.poulastaa.play.presentation.player

import com.poulastaa.core.domain.RepeatState
import com.poulastaa.play.domain.DataLoadingState

data class PlayerUiState(
    val isData: Boolean = false,
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val isPlayerExtended: Boolean = false,

    val queue: List<PlayerUiSong> = emptyList(),
    val info: PlayerUiInfo = PlayerUiInfo()
)

data class PlayerUiInfo(
    val currentPlayingIndex: Int = -1,
    val type: String = "",
    val isShuffledEnabled: Boolean = false,
    val repeatState: RepeatState = RepeatState.IDLE,
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,
)

