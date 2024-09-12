package com.poulastaa.play.domain

sealed interface PlayerEvent {
    data object PlayPause : PlayerEvent
}