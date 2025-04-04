package com.poulastaa.explore.presentation.search.artist

internal sealed interface ExploreArtistUiAction {
    data object OnSearchToggle : ExploreArtistUiAction
    data class OnSearchQueryChange(val message: String) : ExploreArtistUiAction
    data class OnFilterTypeChange(val type: SEARCH_ARTIST_FILTER_TYPE) : ExploreArtistUiAction
}