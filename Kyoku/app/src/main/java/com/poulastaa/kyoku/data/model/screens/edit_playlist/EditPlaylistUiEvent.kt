package com.poulastaa.kyoku.data.model.screens.edit_playlist

sealed class EditPlaylistUiEvent {
    data class EmitToast(val message: String) : EditPlaylistUiEvent()
    data object SomethingWentWrong : EditPlaylistUiEvent()

    data object SearchClick : EditPlaylistUiEvent()
    data class SearchText(val text: String) : EditPlaylistUiEvent()
    data object CancelSearch: EditPlaylistUiEvent()

    data class PlaylistClick(val id: Long) : EditPlaylistUiEvent()
    data object DoneClick : EditPlaylistUiEvent()

    data object NewPlaylistClick : EditPlaylistUiEvent()

    sealed class BottomSheetClick : EditPlaylistUiEvent() {
        data class YesClick(val name: String) : BottomSheetClick()
        data object NoClick : BottomSheetClick()
    }
}
