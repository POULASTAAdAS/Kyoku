package com.poulastaa.network.model

import com.poulastaa.core.data.model.AlbumWithSongDto
import kotlinx.serialization.Serializable

@Serializable
data class AddAlbumDto(
    val list: List<AlbumWithSongDto>,
)
