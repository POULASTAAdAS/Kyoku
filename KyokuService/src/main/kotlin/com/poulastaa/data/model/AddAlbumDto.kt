package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AddAlbumDto(
    val list: List<AlbumWithSongDto> = emptyList()
)
