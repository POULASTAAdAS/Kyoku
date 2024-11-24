package com.poulastaa.core.domain.model

sealed interface PlayerEvent {
    data object PlayPause : PlayerEvent
    data object PlayNext : PlayerEvent
    data object PlayPrev : PlayerEvent
    data class SeekTo(val value: Float) : PlayerEvent
    data class SeekToSong(val index: Int, val pos: Long = 0) : PlayerEvent
    data object Stop : PlayerEvent
}