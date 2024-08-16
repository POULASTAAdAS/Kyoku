package com.poulastaa.network.mapper

import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.domain.model.PlaylistData

fun PlaylistDto.toPlaylistData() = PlaylistData(
    id = this.id,
    name = this.name,
    listOfSong = this.listOfSong.map { it.toSong() }
)