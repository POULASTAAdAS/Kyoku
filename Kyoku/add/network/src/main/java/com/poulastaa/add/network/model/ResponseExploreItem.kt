package com.poulastaa.add.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
internal data class ResponseExploreItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String = "",
    val releaseYear: Int = 0,
    val artist: String? = null,
)