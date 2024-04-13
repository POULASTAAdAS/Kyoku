package com.poulastaa.kyoku.data.model.screens.edit_playlist

sealed class EditPlaylistUiEvent {
    data class EmitToast(val message: String) : EditPlaylistUiEvent()
    data object SomethingWentWrong : EditPlaylistUiEvent()

    data object SearchClick : EditPlaylistUiEvent()
    data class SearchText(val text: String) : EditPlaylistUiEvent()
    data object CancelSearch : EditPlaylistUiEvent()

    data class PlaylistClick(val id: Long) : EditPlaylistUiEvent()
    data object DoneClick : EditPlaylistUiEvent()

    sealed class NewPlaylist : EditPlaylistUiEvent() {
        data object NewPlaylistOpen : NewPlaylist()
        data class NewPlaylistNameEnter(val text: String) : NewPlaylist()
        data object NewPlaylistYes : NewPlaylist()
        data object NewPlaylistNo : NewPlaylist()
    }

    data object FavClick : EditPlaylistUiEvent()
}
