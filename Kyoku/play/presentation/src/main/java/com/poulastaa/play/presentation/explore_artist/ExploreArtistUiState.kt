package com.poulastaa.play.presentation.explore_artist

import com.poulastaa.play.domain.DataLoadingState

data class ExploreArtistUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",

    val data: ExploreArtistData = ExploreArtistData()
)

data class ExploreArtistData(
    val album: List<ExploreArtistSingleData> = emptyList(),
    val song: List<ExploreArtistSingleData> = emptyList()
)

data class ExploreArtistSingleData(
    val id: Long = -1,
    val title: String,
    val coverImage: String = "",
    val releaseYear: Int = 0,
    val isExpanded: Boolean = false
)