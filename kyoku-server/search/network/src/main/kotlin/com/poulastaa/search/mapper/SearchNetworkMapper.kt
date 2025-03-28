package com.poulastaa.search.mapper

import com.poulastaa.core.domain.model.DtoArtistPagingItem
import com.poulastaa.search.model.ResponseArtistPagingItem

internal fun DtoArtistPagingItem.toResponseArtistPagingItem() = ResponseArtistPagingItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    releaseYear = this.releaseYear,
)