package com.poulastaa.setup.presentation.get_spotify_playlist.model

import com.poulastaa.core.presentation.ui.model.UiSong

data class UiPlaylist(
    val id: Long = -1,
    val name: String = "",
    val listOfUiSong: List<UiSong> = emptyList(),
    val isExpanded: Boolean = false,
)