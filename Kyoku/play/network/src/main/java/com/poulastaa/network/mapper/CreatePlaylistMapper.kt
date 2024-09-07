package com.poulastaa.network.mapper

import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.network.model.CreatePlaylistTypeDto

fun CreatePlaylistTypeDto.toCreatePlaylistType() = when (this) {
    CreatePlaylistTypeDto.RECENT_HISTORY -> CreatePlaylistType.RECENT_HISTORY
    CreatePlaylistTypeDto.YOUR_FAVOURITES -> CreatePlaylistType.YOUR_FAVOURITES
    CreatePlaylistTypeDto.SUGGESTED_FOR_YOU -> CreatePlaylistType.SUGGESTED_FOR_YOU
    CreatePlaylistTypeDto.YOU_MAY_ALSO_LIKE -> CreatePlaylistType.YOU_MAY_ALSO_LIKE
}

fun Pair<CreatePlaylistTypeDto, List<SongDto>>.toCreatePlaylistData() = Pair(
    first = this.first.toCreatePlaylistType(),
    second = this.second.map { it.toSong() }
)