package com.poulastaa.add.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
internal data class AddSongToPlaylistPageItemResponse(
    val type: AddSongToPlaylistPageTypeResponse,
    val data: List<AddSongToPlaylistItemResponse> = emptyList(),
)