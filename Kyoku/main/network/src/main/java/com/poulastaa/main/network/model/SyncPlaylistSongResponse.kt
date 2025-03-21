package com.poulastaa.main.network.model

import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import kotlinx.serialization.Serializable

@Serializable
data class SyncPlaylistSongResponse<T>(
    val removeIdList: List<Pair<PlaylistId, List<SongId>>>,
    val newData: List<T>,
)
