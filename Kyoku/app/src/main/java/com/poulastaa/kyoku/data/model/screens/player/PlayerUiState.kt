package com.poulastaa.kyoku.data.model.screens.player

sealed class PlayerUiState {
    data object Initial : PlayerUiState()
    data class Ready(val duration: Long) : PlayerUiState()
    data class Progress(val value: Long) : PlayerUiState()
    data class Buffering(val value: Long) : PlayerUiState()
    data class Playing(val isPlaying: Boolean) : PlayerUiState()

    data class CurrentPlayingSongId(val id: Long) : PlayerUiState()
}