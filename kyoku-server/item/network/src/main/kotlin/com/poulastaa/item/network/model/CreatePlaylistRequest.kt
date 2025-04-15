package com.poulastaa.item.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CreatePlaylistRequest(
    val playlistName: String,
)
