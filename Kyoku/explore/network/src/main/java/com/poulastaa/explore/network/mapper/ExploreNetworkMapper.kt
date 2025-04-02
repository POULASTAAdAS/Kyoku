package com.poulastaa.explore.network.mapper

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.network.model.ResponseExploreItem

internal fun ResponseArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.coverImage
)

internal fun ResponseExploreItem.toDtoAllFromArtistItem() = DtoExploreItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    releaseYear = this.releaseYear,
    artist = this.artist
)