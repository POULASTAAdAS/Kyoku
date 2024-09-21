package com.poulastaa.core.domain.model

sealed interface PlayerState {
    data object Initial : PlayerState
    data class Ready(val totalTime: String) : PlayerState
    data class ProgressBar(val value: Float) : PlayerState
    data class Progress(val value: String) : PlayerState
    data class Buffering(val value: Long) : PlayerState
    data class Playing(val isPlaying: Boolean) : PlayerState

    data class CurrentlyPlaying(
        val songId: Long,
        val hasNext: Boolean = false,
        val hasPrev: Boolean = false,
    ) : PlayerState
}
