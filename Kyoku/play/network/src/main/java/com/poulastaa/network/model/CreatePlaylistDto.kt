package com.poulastaa.network.model

import com.poulastaa.core.data.model.SongDto
import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistDto(
    val data: List<Pair<CreatePlaylistTypeDto, List<SongDto>>> = emptyList(),
)