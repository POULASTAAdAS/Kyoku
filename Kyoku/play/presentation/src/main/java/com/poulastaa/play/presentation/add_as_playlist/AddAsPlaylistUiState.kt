package com.poulastaa.play.presentation.add_as_playlist

import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.model.PrevSong

data class AddAsPlaylistUiState(
    val isMakingApiCall: Boolean = false,
    val isLoading: Boolean = true,
    val header: String = "",
    val name: String = "",
    val prevSong: List<PrevSong> = emptyList(),
    val exploreType: ExploreType = ExploreType.OLD_GEM,
)
