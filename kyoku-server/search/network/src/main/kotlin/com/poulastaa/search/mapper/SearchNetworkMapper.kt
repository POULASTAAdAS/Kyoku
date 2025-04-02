package com.poulastaa.search.mapper

import com.poulastaa.core.domain.model.DtoSearchItem
import com.poulastaa.search.model.ResponseExploreItem

internal fun DtoSearchItem.toResponseExploreItem() = ResponseExploreItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    releaseYear = this.releaseYear,
    artist = this.artist
)