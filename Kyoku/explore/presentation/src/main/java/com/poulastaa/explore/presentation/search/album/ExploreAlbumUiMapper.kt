package com.poulastaa.explore.presentation.search.album

import com.poulastaa.explore.domain.model.DtoExploreAlbumFilterType

internal fun SEARCH_ALBUM_FILTER_TYPE.toDtoExploreAlbumFilterType() = when (this) {
    SEARCH_ALBUM_FILTER_TYPE.MOST_POPULAR -> DtoExploreAlbumFilterType.MOST_POPULAR
    SEARCH_ALBUM_FILTER_TYPE.ARTIST -> DtoExploreAlbumFilterType.ARTIST
    SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR -> DtoExploreAlbumFilterType.RELEASE_YEAR
}