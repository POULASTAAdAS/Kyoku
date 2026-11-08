package com.poulastaa.setup.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPlaylistsResponse(
    val playlists: List<FullPlaylistResponse> = emptyList(),
)
