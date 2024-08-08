package com.poulastaa.play.presentation.add_as_playlist

import com.poulastaa.core.domain.ExploreType

data class PlaylistBottomSheetUiState(
    val isOpen: Boolean = false,
    val exploreType: ExploreType = ExploreType.OLD_GEM,
)
