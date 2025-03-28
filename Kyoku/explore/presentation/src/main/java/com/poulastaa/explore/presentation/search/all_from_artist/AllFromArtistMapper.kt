package com.poulastaa.explore.presentation.search.all_from_artist

import com.poulastaa.explore.domain.model.DtoAllFromArtistItem

internal fun DtoAllFromArtistItem.toAllFromArtistUiItem() = AllFromArtistUiItem(
    id = this.id,
    title = this.title,
    poster = this.poster ?: "",
    releaseYear = this.releaseYear
)