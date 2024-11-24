package com.poulastaa.play.presentation.song_artist

sealed interface SongArtistsUiEvent {
    data class OnArtistClick(val artistId: Long) : SongArtistsUiEvent
}