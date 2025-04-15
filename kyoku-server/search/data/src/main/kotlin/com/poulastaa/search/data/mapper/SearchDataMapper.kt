package com.poulastaa.search.data.mapper

import com.poulastaa.core.domain.model.DtoSearchItem
import com.poulastaa.search.model.DtoAddSongToPlaylistItem
import com.poulastaa.search.model.DtoAddSongToPlaylistItemType

internal fun DtoSearchItem.toDtoAddSongToPlaylistItem(type: DtoAddSongToPlaylistItemType) = DtoAddSongToPlaylistItem(
    id = this.id,
    title = this.title,
    poster = if (this.poster == null) this.posters else listOf(this.poster ?: ""),
    artist = this.artist,
    numbers = this.numbers,
    type = type
)