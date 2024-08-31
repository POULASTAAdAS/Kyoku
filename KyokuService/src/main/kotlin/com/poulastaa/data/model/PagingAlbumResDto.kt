package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingAlbumResDto(
    val list: List<PagingAlbumDto> = emptyList(),
)
