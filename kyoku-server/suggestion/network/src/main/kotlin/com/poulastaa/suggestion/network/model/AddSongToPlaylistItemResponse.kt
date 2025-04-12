package com.poulastaa.suggestion.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AddSongToPlaylistItemResponse(
    val id: Long = -1,
    val title: String = "",
    val poster: List<String> = emptyList(),
    val artist: String? = null,
    val numbers: Long = 0,
    val type: AddSongToPlaylistItemTypeResponse,
)