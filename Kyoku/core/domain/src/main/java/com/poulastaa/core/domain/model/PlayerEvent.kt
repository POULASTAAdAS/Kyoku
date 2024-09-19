package com.poulastaa.core.domain.model

sealed interface PlayerEvent {
    data object PlayPause : PlayerEvent
}