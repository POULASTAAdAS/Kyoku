package com.poulastaa.play.presentation.explore_artist

import com.poulastaa.core.presentation.ui.model.UiArtist

data class ExploreArtistUiState(
    val isPlayingQueue: Boolean = false,
    val header: String = "",
    val list: List<ExploreArtistThreeDotEvent> = emptyList(),
    val artist: UiArtist = UiArtist()
)

data class ExploreArtistSingleUiData(
    val id: Long = -1,
    val title: String,
    val coverImage: String = "",
    val releaseYear: Int = 0,
    val isExpanded: Boolean = false
)