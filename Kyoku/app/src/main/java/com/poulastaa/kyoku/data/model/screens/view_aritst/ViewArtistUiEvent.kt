package com.poulastaa.kyoku.data.model.screens.view_aritst

sealed class ViewArtistUiEvent {
    data class EmitToast(val message: String) : ViewArtistUiEvent()
    data object SomethingWentWrong : ViewArtistUiEvent()

    data class ArtistClick(val name: String) : ViewArtistUiEvent()
}