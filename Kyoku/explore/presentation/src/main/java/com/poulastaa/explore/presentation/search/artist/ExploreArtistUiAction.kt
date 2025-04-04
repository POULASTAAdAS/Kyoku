package com.poulastaa.explore.presentation.search.artist

import com.poulastaa.core.domain.model.ArtistId

internal sealed interface ExploreArtistUiAction {
    data object OnSearchToggle : ExploreArtistUiAction
    data class OnSearchQueryChange(val message: String) : ExploreArtistUiAction
    data class OnFilterTypeChange(val type: SEARCH_ARTIST_FILTER_TYPE) : ExploreArtistUiAction
    data class OnArtistClick(val artistId: ArtistId) : ExploreArtistUiAction
}