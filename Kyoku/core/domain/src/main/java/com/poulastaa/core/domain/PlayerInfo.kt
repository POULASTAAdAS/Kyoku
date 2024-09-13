package com.poulastaa.core.domain

data class PlayerInfo(
    val id: Long,
    val type: String,
    val isShuffledEnabled: Boolean,
    val repeatState: RepeatState = RepeatState.IDLE,
    val isPlaying: Boolean = false,
    val hasNext: Boolean,
    val hasPrev: Boolean,
)
