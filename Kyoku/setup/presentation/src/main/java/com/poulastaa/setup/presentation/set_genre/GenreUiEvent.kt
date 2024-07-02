package com.poulastaa.setup.presentation.set_genre

sealed interface GenreUiEvent {
    data class OnGenreClick(val id: Int) : GenreUiEvent
    data object OnContinueClick : GenreUiEvent
}