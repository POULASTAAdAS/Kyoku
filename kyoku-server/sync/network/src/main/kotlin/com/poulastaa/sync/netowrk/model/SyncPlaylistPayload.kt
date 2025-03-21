package com.poulastaa.sync.netowrk.model

import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId
import kotlinx.serialization.Serializable

@Serializable
data class SyncPlaylistPayload(
    val playlistId: PlaylistId,
    val listOfSongId: List<SongId>,
)