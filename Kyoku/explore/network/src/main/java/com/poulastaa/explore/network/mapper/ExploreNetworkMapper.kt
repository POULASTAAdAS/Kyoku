package com.poulastaa.explore.network.mapper

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.explore.domain.model.DtoAllFromArtistItem
import com.poulastaa.explore.network.model.ResponseAllFromArtistItem

internal fun ResponseArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.coverImage
)

internal fun ResponseAllFromArtistItem.toDtoAllFromArtistItem() = DtoAllFromArtistItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artist = this.artist
)