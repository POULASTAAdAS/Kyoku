package com.poulastaa.suggestion.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AddSongToPlaylistPageItemResponse(
    val type: AddSongToPlaylistPageTypeResponse,
    val data: List<AddSongToPlaylistItemResponse> = emptyList(),
)