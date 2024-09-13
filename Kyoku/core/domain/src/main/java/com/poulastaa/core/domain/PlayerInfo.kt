package com.poulastaa.core.domain

data class PlayerInfo(
    val id: Long = -1,
    val type: String = "",
    val isShuffledEnabled: Boolean = false,
    val repeatState: RepeatState = RepeatState.IDLE,
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,
)
