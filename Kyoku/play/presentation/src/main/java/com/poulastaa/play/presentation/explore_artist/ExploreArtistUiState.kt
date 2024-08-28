package com.poulastaa.play.presentation.explore_artist

import com.poulastaa.core.presentation.ui.model.UiArtist

data class ExploreArtistUiState(
    val header: String = "",
    val artist: UiArtist = UiArtist()
)

data class ExploreArtistSingleUiData(
    val id: Long = -1,
    val title: String,
    val coverImage: String = "",
    val releaseYear: Int = 0,
    val isExpanded: Boolean = false
)