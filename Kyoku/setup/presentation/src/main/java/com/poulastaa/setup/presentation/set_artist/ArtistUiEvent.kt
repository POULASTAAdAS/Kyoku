package com.poulastaa.setup.presentation.set_artist

sealed interface ArtistUiEvent {
    data class OnArtistClick(val id: Long) : ArtistUiEvent
    data object OnContinueClick : ArtistUiEvent
}