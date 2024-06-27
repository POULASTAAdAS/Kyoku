package com.poulastaa.setup.presentation.get_spotify_playlist.model

data class UiPlaylist(
    val id: Long = -1,
    val name: String = "",
    val listOfUiSong: List<UiSong> = emptyList(),
    val isExpanded: Boolean = false,
)