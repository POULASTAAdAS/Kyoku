package com.poulastaa.explore.presentation.search.all_from_artist

import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.presentation.model.ExploreUiItem

internal fun DtoExploreItem.toExploreUiItem() = ExploreUiItem(
    id = this.id,
    title = this.title,
    poster = this.poster ?: "",
    releaseYear = this.releaseYear,
    artist = this.artist
)