package com.poulastaa.kyoku.data.model.screens.setup.suggest_artist

sealed class SuggestArtistUiEvent {
    data class OnArtistClick(val name: String) : SuggestArtistUiEvent()

    data object OnContinueClick : SuggestArtistUiEvent()
    data class EmitToast(val message: String) : SuggestArtistUiEvent()
    data object SomethingWentWrong : SuggestArtistUiEvent()
}