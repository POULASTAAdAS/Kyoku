package com.poulastaa.play.presentation.explore_artist

import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.DataLoadingState

data class ExploreArtistUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",

    val isFollowed: Boolean = false,
    val artist: UiArtist = UiArtist(),
    val data: ExploreArtistData = ExploreArtistData()
)

data class ExploreArtistData(
    val album: List<ExploreArtistSingleUiData> = emptyList(),
    val song: List<ExploreArtistSingleUiData> = emptyList()
)

data class ExploreArtistSingleUiData(
    val id: Long = -1,
    val title: String,
    val coverImage: String = "",
    val releaseYear: Int = 0,
    val isExpanded: Boolean = false
)