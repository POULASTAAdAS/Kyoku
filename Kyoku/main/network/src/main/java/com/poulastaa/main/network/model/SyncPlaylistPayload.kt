package com.poulastaa.main.network.model

import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import kotlinx.serialization.Serializable

@Serializable
data class SyncPlaylistPayload(
    val playlistId: PlaylistId,
    val listOfSongId: List<SongId>,
)
