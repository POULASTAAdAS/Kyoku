package com.poulastaa.kyoku.data.model.screens.create_playlist

sealed class CreatePlaylistUiEvent {
    data class EmitToast(val message: String) : CreatePlaylistUiEvent()
    data object SomethingWentWrong : CreatePlaylistUiEvent()

    data class EnterName(val text: String) : CreatePlaylistUiEvent()

    data object SaveClicked : CreatePlaylistUiEvent()
}