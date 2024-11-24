package com.poulastaa.core.domain

data class PlayerInfo(
    val otherId: Long = -1,
    val title: String = "",
    val type: PlayType = PlayType.IDLE,
    val isShuffledEnabled: Boolean = false,
    val repeatState: RepeatState = RepeatState.IDLE,
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,
)
