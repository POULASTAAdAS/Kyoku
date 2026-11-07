package com.poulastaa.setup.network.model

import com.poulastaa.common.domain.model.DtoPlaylist
import com.poulastaa.common.domain.model.DtoPlaylistVisibility
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistResponse(
    val playlistId: Long,
    val name: String,
    val popularity: Long = 0,
    val status: PlaylistVisibilityResponse = PlaylistVisibilityResponse.PUBLIC,
) {
    fun toDto() = DtoPlaylist(
        id = playlistId,
        name = name,
        popularity = popularity,
        visibility = status.toDto(),
    )
}

@Serializable
enum class PlaylistVisibilityResponse {
    PUBLIC,
    PRIVATE;

    fun toDto() = when (this) {
        PUBLIC -> DtoPlaylistVisibility.PUBLIC
        PRIVATE -> DtoPlaylistVisibility.PRIVATE
    }
}
