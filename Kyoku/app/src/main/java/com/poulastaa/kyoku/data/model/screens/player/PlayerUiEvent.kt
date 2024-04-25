package com.poulastaa.kyoku.data.model.screens.player

sealed class PlayerUiEvent {
    data object PlayPause : PlayerUiEvent()

    data class SelectedSongChange(val index: Int) : PlayerUiEvent()
    data object Backward : PlayerUiEvent()
    data object Forward : PlayerUiEvent()

    data class SeekTo(val index: Long) : PlayerUiEvent()

    data object SeekToPrev : PlayerUiEvent()
    data object SeekToNext : PlayerUiEvent()

    data object Stop : PlayerUiEvent()

    data class UpdateProgress(val value: Float) : PlayerUiEvent()
}