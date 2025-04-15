package com.poulastaa.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
internal data class CreatePlaylistRequest(
    val playlistName: String,
)
