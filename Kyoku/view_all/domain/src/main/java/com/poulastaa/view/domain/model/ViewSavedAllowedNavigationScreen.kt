package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.ArtistId

sealed interface ViewSavedAllowedNavigationScreen {
    data class ExploreArtist(val artistId: ArtistId) : ViewSavedAllowedNavigationScreen
    data class Other(
        val otherId: Long,
        val type: DtoViewSavedItemNavigationType,
    ) : ViewSavedAllowedNavigationScreen
}