package com.poulastaa.search.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseExploreItem(
    val id: Long,
    val title: String,
    val poster: String?,
    val releaseYear: Int?,
    val artist: String? = null,
)