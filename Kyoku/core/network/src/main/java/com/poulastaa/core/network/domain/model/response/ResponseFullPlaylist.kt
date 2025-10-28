package com.poulastaa.core.network.domain.model.response

import com.poulastaa.core.domain.utils.PlaylistId
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFullPlaylist(
    val playlistId: PlaylistId,
    val name: String,
    val songs: List<ResponseSong>,
)
