package com.poulastaa.kyoku.data.model.screens.setup.suggest_genre

sealed class SuggestGenreUiEvent {
    data class OnGenreClick(val name: String) : SuggestGenreUiEvent()

    data object OnContinueClick : SuggestGenreUiEvent()
    data class EmitToast(val message: String) : SuggestGenreUiEvent()
    data object SomethingWentWrong : SuggestGenreUiEvent()
}