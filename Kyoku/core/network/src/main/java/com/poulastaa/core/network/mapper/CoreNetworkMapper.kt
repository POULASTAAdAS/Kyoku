package com.poulastaa.core.network.mapper

import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.network.model.ResponsePlaylist

fun ResponsePlaylist.toDtoPlaylist() = DtoPlaylist(
    id = this.id,
    name = this.name,
    popularity = this.popularity
)