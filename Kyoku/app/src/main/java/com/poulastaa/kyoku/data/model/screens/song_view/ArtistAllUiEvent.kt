package com.poulastaa.kyoku.data.model.screens.song_view

sealed class ArtistAllUiEvent {
    data class EmitToast(val message: String) : ArtistAllUiEvent()
    data object SomethingWentWrong : ArtistAllUiEvent()

    sealed class ItemClick : ArtistAllUiEvent() {
        data class AlbumClick(val id: Long) : ItemClick()
        data class SongClick(val id: Long) : ItemClick()
    }
}
