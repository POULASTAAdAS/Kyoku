package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistPagingDtoWrapper(
    val list: List<CreatePlaylistPagingDto> = emptyList(),
)