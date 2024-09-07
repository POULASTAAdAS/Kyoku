package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistPagingDtoWrapper(
    val list: List<CreatePlaylistPagingDto>
)
