package com.poulastaa.core.database.mapper

import com.poulastaa.core.ViewData
import com.poulastaa.core.database.model.PlaylistResult

fun List<PlaylistResult>.toViewData(id: Long) = ViewData(
    id = id,
    name = this.first().playlistName,
    listOfSong = this.map { it.toPlaylistSong() }
)