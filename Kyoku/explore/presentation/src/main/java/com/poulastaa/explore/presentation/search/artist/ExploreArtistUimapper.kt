package com.poulastaa.explore.presentation.search.artist

import com.poulastaa.explore.domain.model.DtoExploreArtistFilterType

internal fun SEARCH_ARTIST_FILTER_TYPE.toDtoExploreArtistFilterType() = when (this) {
    SEARCH_ARTIST_FILTER_TYPE.ALL -> DtoExploreArtistFilterType.ALL
    SEARCH_ARTIST_FILTER_TYPE.POPULARITY -> DtoExploreArtistFilterType.POPULARITY
}