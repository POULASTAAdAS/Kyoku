package com.poulastaa.play.domain

sealed interface PlayerState {
    data object Initial : PlayerState
    data class Ready(val duration: Long) : PlayerState
    data class Progress(val value: Long) : PlayerState
    data class Buffering(val value: Long) : PlayerState
    data class Playing(val isPlaying: Boolean) : PlayerState

    data class CurrentlyPlaying(
        val id: Long,
        val hasNext: Boolean = false,
        val hasPrev: Boolean = false
    ) : PlayerState
}
