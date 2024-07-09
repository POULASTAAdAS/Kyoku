package com.poulastaa.play.presentation.root_drawer.home.model

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.ui.model.UiArtist

@Stable
data class UiHomeData(
    val popularSongMix: List<UiPrevSong> = emptyList(),
    val popularSongFromYourTime: List<UiPrevSong> = emptyList(),
    val favouriteArtistMix: List<UiPrevSong> = emptyList(),
    val dayTypeSong: List<UiPrevSong> = emptyList(),
    val popularAlbum: List<UiPrevAlbum> = emptyList(),
    val suggestedArtist: List<UiArtist> = emptyList(),
    val popularArtistSong: List<UiArtistWithSong> = emptyList(),
)