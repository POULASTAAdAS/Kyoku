package com.poulastaa.setup.network.model

import com.poulastaa.setup.domain.model.DtoPlaylist
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistResponse(
    val playlistId: Long,
    val name: String,
) {
    fun toDto() = DtoPlaylist(
        id = playlistId,
        name = name,
    )
}
