package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistDto(
    val data: List<Pair<CreatePlaylistTypeDto, List<SongDto>>> = emptyList(),
)
