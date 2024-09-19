package com.poulastaa.core.domain.repository.player

import com.poulastaa.core.domain.model.PlayerEvent
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface KyokuPlayer {
    val playerUiState: StateFlow<PlayerState>

    fun onEvent(event: PlayerEvent)
    fun addMediaItem(song: PlayerSong)
    fun addMediaItem(list: List<PlayerSong>)
}