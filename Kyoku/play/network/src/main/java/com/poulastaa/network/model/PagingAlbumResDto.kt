package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingAlbumResDto(
    val list: List<PagingAlbumDto> = emptyList(),
)
