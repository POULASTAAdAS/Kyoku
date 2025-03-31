package com.poulastaa.search.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtistPagingItem(
    val id: Long,
    val title: String,
    val poster: String?,
    val releaseYear: Int?,
)