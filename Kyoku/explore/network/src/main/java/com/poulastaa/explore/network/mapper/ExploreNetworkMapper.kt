package com.poulastaa.explore.network.mapper

import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.network.model.ResponseExploreItem
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
internal fun ResponseExploreItem.toDtoAllFromArtistItem() = DtoExploreItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    releaseYear = this.releaseYear,
    artist = this.artist
)