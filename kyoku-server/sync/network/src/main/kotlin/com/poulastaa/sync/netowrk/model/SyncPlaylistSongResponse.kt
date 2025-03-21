package com.poulastaa.sync.netowrk.model

import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.network.model.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SyncPlaylistSongResponse(
    val removeIdList: List<Pair<PlaylistId, List<SongId>>>,
    val newData: List<Pair<PlaylistId, List<ResponseSong>>>,
)
