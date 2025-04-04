package com.poulastaa.explore.presentation.search.album

import com.poulastaa.core.domain.model.AlbumId

internal sealed interface ExploreAlbumUiAction {
    data object OnSearchToggle : ExploreAlbumUiAction
    data class OnFilterTypeChange(val type: SEARCH_ALBUM_FILTER_TYPE) : ExploreAlbumUiAction
    data class OnAlbumClick(val albumId: AlbumId) : ExploreAlbumUiAction
    data class OnAlbumThreeDtoClick(val albumId: AlbumId) : ExploreAlbumUiAction
    data class OnSearchQueryChange(val query: String) : ExploreAlbumUiAction
}