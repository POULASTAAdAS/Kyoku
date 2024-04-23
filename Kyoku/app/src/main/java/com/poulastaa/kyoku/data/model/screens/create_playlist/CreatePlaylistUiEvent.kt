package com.poulastaa.kyoku.data.model.screens.create_playlist

import android.content.Context

sealed class CreatePlaylistUiEvent {
    data class EmitToast(val message: String) : CreatePlaylistUiEvent()
    data object SomethingWentWrong : CreatePlaylistUiEvent()

    data class EnterName(val text: String) : CreatePlaylistUiEvent()

    data class SaveClicked(val context: Context) : CreatePlaylistUiEvent()
}