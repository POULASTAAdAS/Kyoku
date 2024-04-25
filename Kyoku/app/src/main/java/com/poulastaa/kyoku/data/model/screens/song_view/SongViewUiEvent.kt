package com.poulastaa.kyoku.data.model.screens.song_view

sealed class SongViewUiEvent {
    data class EmitToast(val message: String) : SongViewUiEvent()
    data object SomethingWentWrong : SongViewUiEvent()

    sealed class ItemClick : SongViewUiEvent() {
        data class ViewAllFromArtist(val name: String) : ItemClick()
    }
}