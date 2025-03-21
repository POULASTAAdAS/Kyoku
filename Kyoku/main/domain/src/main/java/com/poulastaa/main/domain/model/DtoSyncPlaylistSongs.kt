package com.poulastaa.main.domain.model

import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

data class DtoSyncPlaylistSongs<T>(
    val removeList: List<Pair<PlaylistId, List<SongId>>>,
    val newData: List<T>,
)
