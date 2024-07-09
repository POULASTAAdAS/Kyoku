package com.poulastaa.play.presentation.root_drawer.home.model

import com.poulastaa.core.presentation.ui.model.UiArtist

data class UiArtistWithSong(
    val artist: UiArtist = UiArtist(),
    val listOfSong: List<UiSongWithInfo> = emptyList(),
)